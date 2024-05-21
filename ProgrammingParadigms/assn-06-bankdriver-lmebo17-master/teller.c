#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <assert.h>
#include <inttypes.h>
#include "account.c"
#include "teller.h"
#include "account.h"
#include "error.h"
#include "debug.h"
#include <pthread.h>

/*
 * deposit money into an account
 */
int
Teller_DoDeposit(Bank *bank, AccountNumber accountNum, AccountAmount amount){
  assert(amount >= 0);
  DPRINTF('t', ("Teller_DoDeposit(account 0x%"PRIx64" amount %"PRId64")\n",
                accountNum, amount));

  Account *account = Account_LookupByNumber(bank, accountNum);
  
  if (account == NULL) {
    return ERROR_ACCOUNT_NOT_FOUND;
  }
  pthread_mutex_lock(&account->account_lock);
  int branch_id = AccountNum_GetBranchID(accountNum);
  sem_wait(&bank->branches[branch_id].branch_lock);
  Account_Adjust(bank,account, amount, 1);
  pthread_mutex_unlock(&account->account_lock);
  sem_post(&bank->branches[branch_id].branch_lock);
  return ERROR_SUCCESS;
}

/*
 * withdraw money from an account
 */
int
Teller_DoWithdraw(Bank *bank, AccountNumber accountNum, AccountAmount amount){
  assert(amount >= 0);

  DPRINTF('t', ("Teller_DoWithdraw(account 0x%"PRIx64" amount %"PRId64")\n",
                accountNum, amount));

  Account *account = Account_LookupByNumber(bank, accountNum);  
  if (account == NULL) {
    return ERROR_ACCOUNT_NOT_FOUND;
  }
  
  pthread_mutex_lock(&account->account_lock);
  int branch_id = AccountNum_GetBranchID(accountNum);
  sem_wait(&bank->branches[branch_id].branch_lock);
  
  if (amount > Account_Balance(account)) {
    pthread_mutex_unlock(&account->account_lock);
    sem_post(&bank->branches[branch_id].branch_lock);
    return ERROR_INSUFFICIENT_FUNDS;
  }

  Account_Adjust(bank,account, -amount, 1);
  pthread_mutex_unlock(&account->account_lock);
  sem_post(&bank->branches[branch_id].branch_lock);
  return ERROR_SUCCESS;
}

/*
 * do a tranfer from one account to another account
 */
int
Teller_DoTransfer(Bank *bank, AccountNumber srcAccountNum,
                  AccountNumber dstAccountNum,
                  AccountAmount amount){
  assert(amount >= 0);

  DPRINTF('t', ("Teller_DoTransfer(src 0x%"PRIx64", dst 0x%"PRIx64
                ", amount %"PRId64")\n",
                srcAccountNum, dstAccountNum, amount));

  Account *srcAccount = Account_LookupByNumber(bank, srcAccountNum);
  if (srcAccount == NULL) {
    return ERROR_ACCOUNT_NOT_FOUND;
  }

  Account *dstAccount = Account_LookupByNumber(bank, dstAccountNum);
  if (dstAccount == NULL) {
    return ERROR_ACCOUNT_NOT_FOUND;
  }
  if(srcAccountNum == dstAccountNum){
    return ERROR_SUCCESS;
  }
  int updateBranch = !Account_IsSameBranch(srcAccountNum, dstAccountNum);
  int src_branch_id = AccountNum_GetBranchID(srcAccountNum);
  int dst_branch_id = AccountNum_GetBranchID(dstAccountNum);
  if(src_branch_id == dst_branch_id){
      if(srcAccount->accountNumber < dstAccount->accountNumber){
          pthread_mutex_lock(&srcAccount->account_lock);
          pthread_mutex_lock(&dstAccount->account_lock);
      } else {
          pthread_mutex_lock(&dstAccount->account_lock);
          pthread_mutex_lock(&srcAccount->account_lock);
      }
      if(amount > Account_Balance(srcAccount)) {
          pthread_mutex_unlock(&dstAccount->account_lock);
          pthread_mutex_unlock(&srcAccount->account_lock);
          return ERROR_INSUFFICIENT_FUNDS;
    }

    Account_Adjust(bank, srcAccount, -amount, updateBranch);
    Account_Adjust(bank, dstAccount, amount, updateBranch);
    pthread_mutex_unlock(&dstAccount->account_lock);
    pthread_mutex_unlock(&srcAccount->account_lock);
  } else {
      if(src_branch_id < dst_branch_id){
          pthread_mutex_lock(&srcAccount->account_lock);
          pthread_mutex_lock(&dstAccount->account_lock);
          sem_wait(&bank->branches[src_branch_id].branch_lock);
          sem_wait(&bank->branches[dst_branch_id].branch_lock); 
      } else {
          pthread_mutex_lock(&dstAccount->account_lock);
          pthread_mutex_lock(&srcAccount->account_lock);
          sem_wait(&bank->branches[dst_branch_id].branch_lock);
          sem_wait(&bank->branches[src_branch_id].branch_lock);
    }

    if(amount > Account_Balance(srcAccount)) {
        pthread_mutex_unlock(&dstAccount->account_lock);
        pthread_mutex_unlock(&srcAccount->account_lock);
        sem_post(&bank->branches[dst_branch_id].branch_lock);
        sem_post(&bank->branches[src_branch_id].branch_lock);
        return ERROR_INSUFFICIENT_FUNDS;
    }

    Account_Adjust(bank, srcAccount, -amount, updateBranch);
    Account_Adjust(bank, dstAccount, amount, updateBranch);
    pthread_mutex_unlock(&srcAccount->account_lock);
    pthread_mutex_unlock(&dstAccount->account_lock);
    sem_post(&bank->branches[dst_branch_id].branch_lock);
    sem_post(&bank->branches[src_branch_id].branch_lock);
  }


  return ERROR_SUCCESS;
}
