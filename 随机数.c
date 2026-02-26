#include <stdio.h>
#include <stdlib.h>
#include <time.h>
int main()
{
    srand(time(0));
    int i = rand() % 100 + 1;
    int number;
    printf("已经生成了随机数\n");
    do
    {
        printf("请输入数字：");
        scanf("%d",&number);
        if(number>i){
            printf("你输入的数字太大了\n");
        }
        else if(number<i){
            printf("你输入的数字太小了\n");
        }
    } while (i != number);
    printf("恭喜你，猜对了！\n");
    return 0;
}