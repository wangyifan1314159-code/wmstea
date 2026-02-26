#include <stdio.h>

int main()
{
    int num = 0, sun = 0;
    int cunt = 0;

    printf("请输入数字：");
    scanf("%d", &num);
    if (num != -1)
    {
        sun += num;
        cunt++;
        /* code */
    }
    printf("平均数是：%.2f\n", (float)sun / cunt);
    return 0;
}