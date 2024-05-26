#include <stdio.h>
#include <math.h>

#define LN10 2.3025850929940456840179914546844

double ln(double x) {
    double old_sum = 0.0;
    double xmlxpl = (x - 1) / (x + 1);
    double xmlxpl_2 = xmlxpl * xmlxpl;
    double denom = 1.0;
    double frac = xmlxpl;
    double term = frac;
    double sum = term;

    while (sum != old_sum) {
        old_sum = sum;
        denom += 2.0;
        frac *= xmlxpl_2;
        sum += frac / denom;
    }

    return 2.0 * sum;
}

double log10func(double x) {
    return ln(x) / LN10;
}

double log10_factorial(int n) {
    double result = 0.0;
    for (int i = 1; i <= n; ++i) {
        result += log10func(i);
    }
    return result;
}

int main() {
    int number;
    printf("Enter a number: ");
    scanf("%d", &number);
    double log_result = log10_factorial(number);
    int result = (int)log_result + 1;
    printf("Number of digits of %d! is %d\n", number, result);

    return 0;
}
