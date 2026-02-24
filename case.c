#include <stdio.h>

int main()
{
    int i;
    printf("请输入数字\n");
    scanf("%d", &i);
    switch (i)
    {
    case 1:
        printf("你好"); break;
    case 2:
        printf("早上好");
        break;
    case 3:
        printf("晚上好");
        break;
    default:
        printf("？？？？");
        break;
    }
    return 0;
}