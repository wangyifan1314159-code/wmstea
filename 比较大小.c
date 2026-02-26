#include <stdio.h>

int main()
{
    int i;
    printf("请输入数字：\n");
    scanf("%d", &i);
    if (i > 999)
    {
        i = 4;
    }
    else if (i > 99)
    {
        i = 3;
    }
    else if (i > 9)
    {
        i = 2;
    }
    else
    {
        i = 1;
    }
    printf("这个数字是%d位数", i);
    return 0;
}