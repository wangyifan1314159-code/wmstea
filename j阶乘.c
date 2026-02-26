#include <stdio.h>


long long jiecheng(int n){
    if (n==0 || n==1)
    {
        printf("0的阶乘是1\n");
        return 1;
    }else if (n>1)
    {
        int result=1;
        for (int i = 2; i <=n; i++)
        {
            result*=i;
        }
        return result;
    }
    
    
}
int main(){
    int n;
    printf("请输入一个整数：");
    scanf("%d",&n);
    long long result=jiecheng(n);
    printf("%d的阶乘是%lld\n",n,result);
    return 0;
}