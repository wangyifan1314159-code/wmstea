#include <stdio.h>

int add(int a,int b);

int main() {
    int price = 0;
    int sum=0;
    printf("请输入价格: ");
    scanf("%d", &price);
    sum=add(price,100);
    printf("找您%d元\n", sum);

    return 0;
}

int add(int a,int b ){
    return a+b;
}