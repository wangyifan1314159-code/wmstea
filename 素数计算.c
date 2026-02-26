#include <stdio.h>
int main()
{
    // int x;
    // for (x = 2; x < 100; x++)
    // {
    //     int n = 1;
    //     // printf("输入一个数字：");
    //     // scanf("%d", &num);
    //     for (int i = 2; i < x; i++)
    //     {
    //         if (x % i == 0)
    //         {
    //             n = 0;
    //             break;
    //         }
    //     }
    //     if (n == 1)
    //     {
    //         printf("%d ", x);
    //     }
    // }

    int cut;
    while (cut < 50)
    {
        int num = 2;
        int x = 1;
        for (int i = 2; i < num; i++)
        {
            if (num % i == 0)
            {
                x = 0;
                break;
            }
        }
        if (x == 1)
        {
            printf("%d ", num);
            cut++;
        }
        num++;
    }
    printf("\n") return 0;
}