#include <stdio.h>
int main(){
    const double timemoney=8.25;
    const int Hour=40;
    double pay=0.0;
    int hour;
    //输入工作时长
    printf("输入工作时长：");
    scanf("%d",&hour);

    //计算工资
    if(hour>=Hour){
        pay=(Hour*timemoney)+(hour-Hour)*(timemoney*1.5);
    }else{
        pay=hour*timemoney;
    }   
     printf("一周工资为：%f",pay);

    return 0;
}