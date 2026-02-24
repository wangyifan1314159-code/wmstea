#include <stdio.h>

int main(){
    int a ,b;
    int c=0;
    scanf("%d %d",&a,&b);
    c=a+b;
    printf("%d + %d = %d\n",a,b,c);
    c=0;
    c=a-b;
    printf("%d - %d = %d\n",a,b,c);
    c=0;
    c=a*b;
    printf("%d * %d = %d\n",a,b,c);
    c=0;
    c=a/b;
    printf("%d / %d = %d\n",a,b,c);
    return 0;
}