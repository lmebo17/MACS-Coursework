#include <assert.h>
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "hashset.h"

#include <curl/curl.h>

#include "bool.h"
#include "html-utils.h"
#include "streamtokenizer.h"
#include "url.h"

static void Welcome(const char *welcomeTextFileName);
static void BuildIndices(const char *feedsFileName , hashset* savedWords , hashset* stopWord, hashset* scanned);
static void ProcessFeed(const char *remoteDocumentName , hashset* savedWords , hashset* stopWords, hashset* scanned);
static void PullAllNewsItems(FILE *dataStream , hashset* savedWords , hashset* stopWords, hashset* scanned);
static bool GetNextItemTag(streamtokenizer *st);
static void ProcessSingleNewsItem(streamtokenizer *st , hashset* savedWords , hashset* stopWords, hashset* scanned);
static void ExtractElement(streamtokenizer *st, const char *htmlTag,
                           char dataBuffer[], int bufferLength);
static void ParseArticle(const char *articleTitle, const char *articleDescription, const char *articleURL , hashset* savedWords , hashset* stopWords, hashset* scanned);
static void ScanArticle(streamtokenizer *st, const char *articleTitle, const char *unused, const char *articleURL , hashset* savedWords , hashset* stopWords, hashset* scanned);
static void QueryIndices(hashset* savedWords , hashset* stopWords);
static void ProcessResponse(const char *word , hashset* savedWords , hashset* stopWords);
static bool WordIsWellFormed(const char *word);
static const signed long kHashMultiplier = -1664117991L;
static const char* stopFile = "data/stop-words.txt";


typedef struct{
  char* title;
  char* url;
  int count;
} Site;

typedef struct{
  char* word;
  vector* sites;
} Word;


static int siteCompareFn(const void* elem1 , const void* elem2){
  Site* first = (Site*)elem1;
  Site* second = (Site*)elem2;
  return strcasecmp(first->title , second->title) || strcasecmp(first->url , second->url);
  
}

static int samibroli (const void* elem, int numBuckets){          
  unsigned long hashcode = 0;
  const char* s = ((Site*)elem)->url;
  for (int i = 0; i < strlen(s); i++)  
    hashcode = hashcode * kHashMultiplier + tolower(s[i]);  
  
  return hashcode % numBuckets;                                
}

static int siteCountFn(const void* elem1 , const void* elem2){
  Site* first = (Site*)elem1;
  Site* second = (Site*)elem2;
  if(first->count == second->count) return 0;
  return first->count > second->count;
}

static void siteFreeFn(void* elem){
  Site* tmp = (Site*)elem;
  free(tmp->title);
  free(tmp->url);
}



static void freeFn(void* elem){
  free((char*)elem);
}

static int hashFn(const void* elem, int numBuckets){          
  unsigned long hashcode = 0;
  const char* s = (char*)elem;
  for (int i = 0; i < strlen(s); i++)  
    hashcode = hashcode * kHashMultiplier + tolower(s[i]);  
  
  return hashcode % numBuckets;                                
}


static int compareFn(const void* elem1 , const void* elem2){
  char* first = (char*)elem1;
  char* second = (char*)elem2;
  return strcasecmp(first , second);
}

static int wordsHashFn(const void* elem , int numBuckets){
  Word* tmp = (Word*)elem;
  return hashFn(tmp->word , numBuckets);
}
static int wordsCompareFn(const void* elem1 , const void* elem2){
  Word* first = (Word*)elem1;
  Word* second = (Word*)elem2;
  return compareFn((first->word) , (second->word));
}

static void wordsFreeFn(void* elem){
  Word* tmp = (Word*)elem;
  free(tmp->word);
  VectorDispose(tmp->sites);
  free(tmp->sites);
}

static void fillHashSetWithStopWords(hashset* stopWords){
  FILE* infile;
  streamtokenizer st;
  char buffer[1009];
  infile = fopen(stopFile , "r");
  assert(infile != NULL);
  STNew(&st , infile , "\r\n" , true);
  while (STNextToken(&st , buffer , sizeof(buffer))){
      HashSetEnter(stopWords , buffer);
  }
  STDispose(&st);
  fclose(infile);
}

/**
 * Function: main
 * --------------
 * Serves as the entry point of the full application.
 * You'll want to update main to declare several hashsets--
 * one for stop words, another for previously seen urls, etc--
 * and pass them (by address) to BuildIndices and QueryIndices.
 * In fact, you'll need to extend many of the prototypes of the
 * supplied helpers functions to take one or more hashset *s.
 *
 * Think very carefully about how you're going to keep track of
 * all of the stop words, how you're going to keep track of
 * all the previously seen articles, and how you're going to
 * map words to the collection of news articles where that
 * word appears.
 */

static const int smallNumBuckets = 1009;
static const int bigNumBuckets = 10007;
static const char *const kWelcomeTextFile = "data/welcome.txt";
static const char *const kDefaultFeedsFile = "data/rss-feeds-tiny.txt";
static const char *const kFilePrefix = "file://";
static const char *const kTextDelimiters =
    " \t\n\r\b!@$%^*()_+={[}]|\\'\":;/?.>,<~`";


int main(int argc, char **argv) {
  setbuf(stdout, NULL);
  curl_global_init(CURL_GLOBAL_DEFAULT);
  
  hashset stopWords;
  char words[1024];
  HashSetNew(&stopWords , sizeof(words) , smallNumBuckets , hashFn , compareFn , NULL);
  fillHashSetWithStopWords(&stopWords);
  Welcome(kWelcomeTextFile);

  hashset savedWords;
  HashSetNew(&savedWords , sizeof(Word) , bigNumBuckets , wordsHashFn , wordsCompareFn , wordsFreeFn);

  hashset scanned;
  HashSetNew(&scanned , sizeof(Site) , smallNumBuckets , samibroli , siteCompareFn , siteFreeFn);
  
  BuildIndices((argc == 1) ? kDefaultFeedsFile : argv[1] , &savedWords , &stopWords , &scanned);
  QueryIndices(&savedWords , &stopWords);
  curl_global_cleanup();
  HashSetDispose(&stopWords);
  HashSetDispose(&savedWords);
  HashSetDispose(&scanned);
  return 0;
}

size_t SavePage(char *ptr, size_t size, size_t nmemb, void *data) {
  return fprintf((FILE *)data, "%s", ptr);
}

static FILE *RemoveCData(const char *tmpFile) {
  FILE *inp = fopen(tmpFile, "rb");
  fseek(inp, 0, SEEK_END);
  long fsize = ftell(inp);
  fseek(inp, 0, SEEK_SET); /* same as rewind(f); */
  char *contents = malloc(fsize + 1);
  long read = fread(contents, 1, fsize, inp);
  assert(fsize == read);
  fclose(inp);
  FILE *out = fopen(tmpFile, "w");
  bool inside_cdata = false;
  for (int i = 0; i < fsize; ++i) {
    if (strncasecmp(contents + i, "<![CDATA[", strlen("<![CDATA[")) == 0) {
      inside_cdata = true;
      i += strlen("<![CDATA[") - 1;
    } else if (inside_cdata && strncmp(contents + i, "]]>", 3) == 0) {
      inside_cdata = false;
      i += 2;
    } else {
      fprintf(out, "%c", contents[i]);
    }
  }
  fclose(out);
  free(contents);
  return fopen(tmpFile, "r");
}

static FILE *FetchURL(const char *path, const char *tmpFile) {
  FILE *tmpDoc = fopen(tmpFile, "w");
  CURL *curl;
  CURLcode res;
  curl = curl_easy_init();
  curl_easy_setopt(curl, CURLOPT_VERBOSE, 0L);
  curl_easy_setopt(curl, CURLOPT_URL, path);
  curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);
  curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, SavePage);
  curl_easy_setopt(curl, CURLOPT_WRITEDATA, tmpDoc);
  res = curl_easy_perform(curl);
  fclose(tmpDoc);
  curl_easy_cleanup(curl);
  if (res != CURLE_OK) {
    return NULL;
  }
  return RemoveCData(tmpFile);
}


static void sortingSites(void* elem , void* auxData){
  Word* word = (Word*)elem;
  VectorSort(word->sites , siteCountFn);
}


/**
 * Function: Welcome
 * -----------------
 * Displays the contents of the specified file, which
 * holds the introductory remarks to be printed every time
 * the application launches.  This type of overhead may
 * seem silly, but by placing the text in an external file,
 * we can change the welcome text without forcing a recompilation and
 * build of the application.  It's as if welcomeTextFileName
 * is a configuration file that travels with the application.
 */

static const char *const kNewLineDelimiters = "\r\n";
static void Welcome(const char *welcomeTextFileName) {
  FILE *infile;
  streamtokenizer st;
  char buffer[1024];

  infile = fopen(welcomeTextFileName, "r");
  assert(infile != NULL);

  STNew(&st, infile, kNewLineDelimiters, true);
  while (STNextToken(&st, buffer, sizeof(buffer))) {
    printf("%s\n", buffer);
  }

  printf("\n");
  STDispose(&st); // remember that STDispose doesn't close the file, since STNew
                  // doesn't open one..
  fclose(infile);
}

/**
 * Function: BuildIndices
 * ----------------------
 * As far as the user is concerned, BuildIndices needs to read each and every
 * one of the feeds listed in the specied feedsFileName, and for each feed parse
 * content of all referenced articles and store the content in the hashset of
 * indices. Each line of the specified feeds file looks like this:
 *
 *   <feed name>: <URL of remore xml document>
 *
 * Each iteration of the supplied while loop parses and discards the feed name
 * (it's in the file for humans to read, but our aggregator doesn't care what
 * the name is) and then extracts the URL.  It then relies on ProcessFeed to
 * pull the remote document and index its content.
 */

static void BuildIndices(const char *feedsFileName , hashset* savedWords , hashset* stopWords, hashset* scanned) {
  FILE *infile;
  streamtokenizer st;
  char remoteFileName[1024];

  infile = fopen(feedsFileName, "r");
  assert(infile != NULL);
  STNew(&st, infile, kNewLineDelimiters, true);
  while (STSkipUntil(&st, ":") !=
         EOF) { // ignore everything up to the first selicolon of the line
    STSkipOver(
        &st,
        ": "); // now ignore the semicolon and any whitespace directly after it
    STNextToken(&st, remoteFileName, sizeof(remoteFileName));
    ProcessFeed(remoteFileName , savedWords , stopWords, scanned);
  }
  HashSetMap(savedWords , sortingSites , NULL);
  STDispose(&st);
  fclose(infile);
  printf("\n");
}

/** * Function: ProcessFeedFromFile * --------------------- * ProcessFeed
 * locates the specified RSS document, from locally */

static void ProcessFeedFromFile(char *fileName , hashset* savedWords , hashset* stopWords, hashset* scanned) {
  FILE *infile;
  streamtokenizer st;
  char articleDescription[1024];
  articleDescription[0] = '\0';
  infile = fopen((const char *)fileName, "r");
  assert(infile != NULL);
  STNew(&st, infile, kTextDelimiters, true);
  ScanArticle(&st, (const char *)fileName, articleDescription, (const char *)fileName , savedWords , stopWords, scanned);
  STDispose(&st); // remember that STDispose doesn't close the file, since STNew
                  // doesn't open one..
  fclose(infile);
}

/**
 * Function: ProcessFeed
 * ---------------------
 * ProcessFeed locates the specified RSS document, and if a (possibly
 * redirected) connection to that remote document can be established, then
 * PullAllNewsItems is tapped to actually read the feed.  Check out the
 * documentation of the PullAllNewsItems function for more information, and
 * inspect the documentation for ParseArticle for information about what the
 * different response codes mean.
 */

static void ProcessFeed(const char *remoteDocumentName , hashset* savedWords , hashset* stopWords, hashset* scanned) {

  if (!strncmp(kFilePrefix, remoteDocumentName, strlen(kFilePrefix))) {
    ProcessFeedFromFile((char *)remoteDocumentName + strlen(kFilePrefix) , savedWords , stopWords, scanned);
    return;
  }

  FILE *tmpFeed = FetchURL(remoteDocumentName, "tmp_feed");
  PullAllNewsItems(tmpFeed , savedWords , stopWords, scanned);
  fclose(tmpFeed);
}

/**
 * Function: PullAllNewsItems
 * --------------------------
 * Steps though the data of what is assumed to be an RSS feed identifying the
 * names and URLs of online news articles.  Check out
 * "datafiles/sample-rss-feed.txt" for an idea of what an RSS feed from the
 * www.nytimes.com (or anything other server that syndicates is stories).
 *
 * PullAllNewsItems views a typical RSS feed as a sequence of "items", where
 * each item is detailed using a generalization of HTML called XML.  A typical
 * XML fragment for a single news item will certainly adhere to the format of
 * the following example:
 *
 * <item>
 *   <title>At Installation Mass, New Pope Strikes a Tone of Openness</title>
 *   <link>http://www.nytimes.com/2005/04/24/international/worldspecial2/24cnd-pope.html</link>
 *   <description>The Mass, which drew 350,000 spectators, marked an important
 * moment in the transformation of Benedict XVI.</description> <author>By IAN
 * FISHER and LAURIE GOODSTEIN</author> <pubDate>Sun, 24 Apr 2005 00:00:00
 * EDT</pubDate> <guid
 * isPermaLink="false">http://www.nytimes.com/2005/04/24/international/worldspecial2/24cnd-pope.html</guid>
 * </item>
 *
 * PullAllNewsItems reads and discards all characters up through the opening
 * <item> tag (discarding the <item> tag as well, because once it's read and
 * indentified, it's been pulled,) and then hands the state of the stream to
 * ProcessSingleNewsItem, which handles the job of pulling and analyzing
 * everything up through and including the </item> tag. PullAllNewsItems
 * processes the entire RSS feed and repeatedly advancing to the next <item> tag
 * and then allowing ProcessSingleNewsItem do process everything up until
 * </item>.
 */

static void PullAllNewsItems(FILE *dataStream , hashset* savedWords , hashset* stopWords, hashset* scanned) {
  streamtokenizer st;
  STNew(&st, dataStream, kTextDelimiters, false);
  while (GetNextItemTag(
      &st)) { // if true is returned, then assume that <item ...> has just been
              // read and pulled from the data stream
    ProcessSingleNewsItem(&st , savedWords , stopWords, scanned);
  }

  STDispose(&st);
}

/**
 * Function: GetNextItemTag
 * ------------------------
 * Works more or less like GetNextTag below, but this time
 * we're searching for an <item> tag, since that marks the
 * beginning of a block of HTML that's relevant to us.
 *
 * Note that each tag is compared to "<item" and not "<item>".
 * That's because the item tag, though unlikely, could include
 * attributes and perhaps look like any one of these:
 *
 *   <item>
 *   <item rdf:about="Latin America reacts to the Vatican">
 *   <item requiresPassword=true>
 *
 * We're just trying to be as general as possible without
 * going overboard.  (Note that we use strncasecmp so that
 * string comparisons are case-insensitive.  That's the case
 * throughout the entire code base.)
 */

static const char *const kItemTagPrefix = "<item";
static bool GetNextItemTag(streamtokenizer *st) {
  char htmlTag[1024];
  while (GetNextTag(st, htmlTag, sizeof(htmlTag))) {
    if (strncasecmp(htmlTag, kItemTagPrefix, strlen(kItemTagPrefix)) == 0) {
      return true;
    }
  }
  return false;
}

/**
 * Function: ProcessSingleNewsItem
 * -------------------------------
 * Code which parses the contents of a single <item> node within an RSS/XML
 * feed. At the moment this function is called, we're to assume that the <item>
 * tag was just read and that the streamtokenizer is currently pointing to
 * everything else, as with:
 *
 *      <title>Carrie Underwood takes American Idol Crown</title>
 *      <description>Oklahoma farm girl beats out Alabama rocker Bo Bice and
 * 100,000 other contestants to win competition.</description>
 *      <link>http://www.nytimes.com/frontpagenews/2841028302.html</link>
 *   </item>
 *
 * ProcessSingleNewsItem parses everything up through and including the </item>,
 * storing the title, link, and article description in local buffers long enough
 * so that the online new article identified by the link can itself be parsed
 * and indexed.  We don't rely on <title>, <link>, and <description> coming in
 * any particular order.  We do asssume that the link field exists (although we
 * can certainly proceed if the title and article descrption are missing.) There
 * are often other tags inside an item, but we ignore them.
 */

static const char *const kItemEndTag = "</item>";
static const char *const kTitleTagPrefix = "<title";
static const char *const kDescriptionTagPrefix = "<description";
static const char *const kLinkTagPrefix = "<link";
static void ProcessSingleNewsItem(streamtokenizer *st , hashset* savedWords , hashset* stopWords, hashset* scanned) {
  char htmlTag[1024];
  char articleTitle[1024];
  char articleDescription[1024];
  char articleURL[1024];
  articleTitle[0] = articleDescription[0] = articleURL[0] = '\0';

  while (GetNextTag(st, htmlTag, sizeof(htmlTag)) &&
         (strcasecmp(htmlTag, kItemEndTag) != 0)) {
    if (strncasecmp(htmlTag, kTitleTagPrefix, strlen(kTitleTagPrefix)) == 0) {
      ExtractElement(st, htmlTag, articleTitle, sizeof(articleTitle));
    }
    if (strncasecmp(htmlTag, kDescriptionTagPrefix,
                    strlen(kDescriptionTagPrefix)) == 0)
      ExtractElement(st, htmlTag, articleDescription,
                     sizeof(articleDescription));
    if (strncasecmp(htmlTag, kLinkTagPrefix, strlen(kLinkTagPrefix)) == 0)
      ExtractElement(st, htmlTag, articleURL, sizeof(articleURL));
  }

  if (strncmp(articleURL, "", sizeof(articleURL)) == 0)
    return; // punt, since it's not going to take us anywhere
  ParseArticle(articleTitle, articleDescription, articleURL , savedWords , stopWords, scanned);
}

/**
 * Function: ExtractElement
 * ------------------------
 * Potentially pulls text from the stream up through and including the matching
 * end tag.  It assumes that the most recently extracted HTML tag resides in the
 * buffer addressed by htmlTag.  The implementation populates the specified data
 * buffer with all of the text up to but not including the opening '<' of the
 * closing tag, and then skips over all of the closing tag as irrelevant.
 * Assuming for illustration purposes that htmlTag addresses a buffer containing
 * "<description" followed by other text, these three scanarios are handled:
 *
 *    Normal Situation:
 * <description>http://some.server.com/someRelativePath.html</description>
 *    Uncommon Situation:   <description></description>
 *    Uncommon Situation:   <description/>
 *
 * In each of the second and third scenarios, the document has omitted the data.
 * This is not uncommon for the description data to be missing, so we need to
 * cover all three scenarious (I've actually seen all three.) It would be quite
 * unusual for the title and/or link fields to be empty, but this handles those
 * possibilities too.
 */

static void ExtractElement(streamtokenizer *st, const char *htmlTag,
                           char dataBuffer[], int bufferLength) {
  assert(htmlTag[strlen(htmlTag) - 1] == '>');
  if (htmlTag[strlen(htmlTag) - 2] == '/')
    return; // e.g. <description/> would state that a description is not being
            // supplied
  STNextTokenUsingDifferentDelimiters(st, dataBuffer, bufferLength, "<");
  RemoveEscapeCharacters(dataBuffer);
  if (dataBuffer[0] == '<')
    strcpy(dataBuffer, ""); // e.g. <description></description> also means
                            // there's no description
  STSkipUntil(st, ">");
  STSkipOver(st, ">");
}

/**
 * Function: ParseArticle
 * ----------------------
 * Attempts to establish a network connect to the news article identified by the
 * three parameters.  The network connection is either established of not.  The
 * implementation is prepared to handle a subset of possible (but by far the
 * most common) scenarios, and those scenarios are categorized by response code:
 *
 *    0 means that the server in the URL doesn't even exist or couldn't be
 * contacted. 200 means that the document exists and that a connection to that
 * very document has been established. 301 means that the document has moved to
 * a new location 302 also means that the document has moved to a new location
 *    4xx and 5xx (which are covered by the default case) means that either
 *        we didn't have access to the document (403), the document didn't exist
 * (404), or that the server failed in some undocumented way (5xx).
 *
 * The are other response codes, but for the time being we're punting on them,
 * since no others appears all that often, and it'd be tedious to be fully
 * exhaustive in our enumeration of all possibilities.
 */

static void ParseArticle(const char *articleTitle,
                         const char *articleDescription,
                         const char *articleURL , hashset* savedWords , hashset* stopWords, hashset* scanned) {
  FILE *tmpDoc = FetchURL(articleURL, "tmp_doc");
  if (tmpDoc == NULL) {
    printf("Unable to fetch URL: %s\n", articleURL);
    return;
  }
  printf("Scanning \"%s\"\n", articleTitle);
  streamtokenizer st;
  STNew(&st, tmpDoc, kTextDelimiters, false);
  ScanArticle(&st, articleTitle, articleDescription, articleURL , savedWords , stopWords, scanned);
  STDispose(&st);
  fclose(tmpDoc);
}

static void addNewWord(const char* articleTittle , const char* articleUrl , hashset* savedWords , const Word* word){
  vector* sites = malloc(sizeof(vector));
  VectorNew(sites , sizeof(Site) , siteFreeFn , smallNumBuckets);
  Site toAdd;
  toAdd.count = 1;
  toAdd.title = strdup(articleTittle);
  toAdd.url = strdup(articleUrl);
  VectorAppend(sites , &toAdd);
  Word newWord; 
  newWord.sites = sites;
  newWord.word = strdup(word->word);
  HashSetEnter(savedWords , &newWord);
}


static void changeHashSet(const char* articleTiTle , const char* articleUrl , Word* word){
  vector* sites = word->sites;
  Site toLookUp;
  toLookUp.title = strdup(articleTiTle);
  toLookUp.url = strdup(articleUrl);
  int index = VectorSearch(sites , &toLookUp , siteCompareFn , 0 , false);
  if(index == -1){
    toLookUp.count = 1;
    VectorAppend(sites , &toLookUp);
  } else {
    ((Site*)VectorNth(sites , index))->count++;
    free(toLookUp.title);
    free(toLookUp.url);
  }
}

/**
 * Function: ScanArticle
 * ---------------------
 * Parses the specified article, skipping over all HTML tags, and counts the
 * numbers of well-formed words that could potentially serve as keys in the set
 * of indices. Once the full article has been scanned, the number of well-formed
 * words is printed, and the longest well-formed word we encountered along the
 * way is printed as well.
 *
 * This is really a placeholder implementation for what will ultimately be
 * code that indexes the specified content.
 */

static void ScanArticle(streamtokenizer *st, const char *articleTitle,
                        const char *unused, const char *articleURL , hashset* savedWords , hashset* stopWords, hashset* scanned) {
  Site newArticle;
  newArticle.url = strdup(articleURL);
  newArticle.title = strdup(articleTitle);
  newArticle.count = 0;
  if (HashSetLookup(scanned, &newArticle) != NULL){
      free(newArticle.url);
      free(newArticle.title);
      return;
  }
  HashSetEnter(scanned, &newArticle);
  int numWords = 0;
  char word[1024];
  char longestWord[1024] = {'\0'};

  while (STNextToken(st, word, sizeof(word))) {
    if (strcasecmp(word, "<") == 0) {
      SkipIrrelevantContent(st); // in html-utls.h
    } else {
      RemoveEscapeCharacters(word);
      char* ch = strdup(word);
      Word tmp;
      tmp.word = strdup(word);
      if (WordIsWellFormed(word) && HashSetLookup(stopWords , ch) == NULL) {
        numWords++;
        Word* wordToAdd = HashSetLookup(savedWords , &tmp);
        if(wordToAdd == NULL){
            addNewWord(articleTitle , articleURL , savedWords , &tmp);
        } else changeHashSet(articleTitle , articleURL , wordToAdd);   
        if (strlen(word) > strlen(longestWord))
          strcpy(longestWord, word);
      }
      free(tmp.word);
      free(ch);
    }  
  }

  printf("\tWe counted %d well-formed words [including duplicates].\n",
         numWords);
  printf("\tThe longest word scanned was \"%s\".", longestWord);
  if (strlen(longestWord) >= 15 && (strchr(longestWord, '-') == NULL))
    printf(" [Ooooo... long word!]");
  printf("\n");
}

/**
 * Function: QueryIndices
 * ----------------------
 * Standard query loop that allows the user to specify a single search term, and
 * then proceeds (via ProcessResponse) to list up to 10 articles (sorted by
 * relevance) that contain that word.
 */

static void QueryIndices(hashset* savedWords , hashset* stopWords) {
  char response[1024];
  while (true) {
    printf("Please enter a single search term [enter to break]: ");
    fgets(response, sizeof(response), stdin);
    response[strlen(response) - 1] = '\0';
    if (strcasecmp(response, "") == 0)
      break;
    ProcessResponse(response , savedWords , stopWords);
  }
}

/**
 * Function: ProcessResponse
 * -------------------------
 * Placeholder implementation for what will become the search of a set of
 * indices for a list of web documents containing the specified word.
 */

static void ProcessResponse(const char *word , hashset* savedWords , hashset* stopWords) {
  if (WordIsWellFormed(word)) {
      if(HashSetLookup(stopWords , word) == NULL){
        Word tmp;
        tmp.word = strdup(word);
        Word* curr = HashSetLookup(savedWords , &tmp);
        free(tmp.word);
        if(curr != NULL){
            int sitesCount = 10;
            int actualSitesCount = VectorLength(curr->sites);
            if(actualSitesCount < 10) sitesCount = actualSitesCount;
           // printf("Nice! We found %d articles that include the word \"%s\".\n", sitesCount, word);
            for(int i = sitesCount; i >= 1; i--){
                Site* cur = VectorNth(curr->sites , i-1);
                if(cur->count > 1){
                  printf("%d.) \"%s\" [search term occurs %d times]\n", sitesCount + 1 - i, cur->title, cur->count);
                } else {
                  printf("%d.) \"%s\" [search term occurs %d time]\n", sitesCount + 1 - i, cur->title, cur->count);
                }
                  printf("\"%s\"\n" , cur->url);
            }
        } else printf("None of today's news articles contain the word \"%s\".\n",word); 
      } else printf("%s\n","Too common a word to be taken seriously. Try something more specific."); 
  } else {
    printf(
        "\tWe won't be allowing words like \"%s\" into our set of indices.\n",
        word);
  }
}

/**
 * Predicate Function: WordIsWellFormed
 * ------------------------------------
 * Before we allow a word to be inserted into our map
 * of indices, we'd like to confirm that it's a good search term.
 * One could generalize this function to allow different criteria, but
 * this version hard codes the requirement that a word begin with
 * a letter of the alphabet and that all letters are either letters, numbers,
 * or the '-' character.
 */

static bool WordIsWellFormed(const char *word) {
  int i;
  if (strlen(word) == 0)
    return true;
  if (!isalpha((int)word[0]))
    return false;
  for (i = 1; i < strlen(word); i++)
    if (!isalnum((int)word[i]) && (word[i] != '-'))
      return false;

  return true;
}
