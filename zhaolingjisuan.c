#include <stdio.h>
int main(){
int money=0;
int price=0;
int num=0;
printf("请输入金额：");
scanf("%d",&price);
printf("请输入面额：");
scanf("%d",&money);
if (money-price<0)
{
    /* code */
    printf("您给的钱不够，请补齐。补齐金额为：%d",(money-price)*-1);
}else{
printf("找零：%d",money-price);

}



return 0;
}