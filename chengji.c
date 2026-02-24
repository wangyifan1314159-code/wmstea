#include <stdio.h>

typedef enum
{
    A,
    B,
    C,
    D,
    E
} grdle;

grdle getnum(int num)
{
    if (num >= 90)
    {
        return A;
    }
    else if (num >= 80 && num < 90)
    {
        return B;
    }
    else if (num >= 70 && num < 80)
    {
        return C;
    }
    else if (num >= 60 && num < 70)
    {
        return D;
    }
    else
    {
        return E;
    }
}

int main()
{
    int i;
    printf("请输入成绩：");
    scanf("%d", &i);
    grdle g=getnum(i);
    switch (g)
    {
    case A:
        printf("A");
        break;
    case B:
        printf("B");
        break;
    case C:
        printf("C");
        break;
    case D:
        printf("D");
        break;
    default:
        printf("E");
        break;
    }
    return 0;
}