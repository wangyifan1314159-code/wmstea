#include <stdio.h>

int main()
{
    // int amount = 100;
    // int price = 0;
    // // printf("请输入金额：");
    // // scanf("%d", &price);
    // // printf("请输入面额：");
    // // scanf("%d", &amount);

    // // int num = amount - price;
    // // printf("找你给你%d", num);

    // int a=0;
    // int b=0;
    // printf("请输入两个整数：");
    // scanf("%d %d", &a, &b);
    // printf("%d + %d = %d",a,b,a+b);

    // double foot;
    // double inch;
    // scanf("%lf %lf",&foot,&inch);
    // printf("%f",(foot * inch)*12);

    // 计算时间差
    int hour1, min1;
    int hour2, min2;
    printf("请输入时间");
    scanf("%d %d %d %d", &hour1, &min2, &hour2, &min2);
    int t1 = hour1 * 60 + min1;
    int t2 = hour2 * 60 + min2;
    int t = t2 - t1;
    printf("时间差为%d小时%d分", t / 60, t % 60);
     return 0;
}
