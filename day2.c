#include <stdio.h>
int main()
{
    // int a=0;
    // scanf("%d",&a);
    // int foot =a/30.48;
    // int icch=((a/30.48)-foot)*12;
    // printf("%d %d",foot,icch);

    // int time = 0, minute = 0;
    // scanf("%d %d", &time, &minute);
    // int hour = time / 100;      // 小时
    // int min = time % 100;       // 分割分钟
    // int mint = hour * 60 + min; // 一共多少前面时间分钟
    // int addmin = minute + mint; // 加上后面的时间
    // hour = addmin / 60;
    // min = addmin % 60;         // 转换小时和分钟
    // printf("%d%d", hour, min); // 拼接时间
    //逆序数
    int num;
    scanf("%d",&num);
    int a=num/100;
    int b=num%10;
    int c=num/10%10;
    num=c*10+b*100+a;
    printf("%d",num);
    return 0;
}