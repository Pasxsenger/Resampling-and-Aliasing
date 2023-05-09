# README

## Part 1 –Spatial Resampling and Aliasing

To run Mypart1.java, please refer to the example commands below:

**javac Mypart1.java**

**java Mypart1 160 0.5 0**



## Part 2 –Temporal Aliasing

To run Mypart2.java, please refer to the example commands below:

**javac Mypart2.java**

**java Mypart2 64 10 25**



## Part 3 (Optional Extra Credit)

<u>According to TA's explanation, the fifth parameter(scale factor) should be like the one from Part 1, which is a decimal less than or equal to 1.0, instead of a number greater than or equal to one like the description file gives.</u>

To run MyExtraCredit.java, please refer to the example commands below:

**javac MyExtraCredit.java**

**java MyExtraCredit 64 10 25 1 0.8**



----







# Analysis Questions for part 1

## Question 1

1. Let’s try an experiment where s (scale factor) remains constant and n (number of lines) is allowed to vary. Comment on your results by using various constant values of *s* for changing *n*. You may attach results, plot charts etc. to qualify your results.

**Command 1: java Mypart1 64 0.5 0**

![image-20230213023006813](/Users/pasxsenger/Library/Application Support/typora-user-images/image-20230213023006813.png)



**Command 2: java Mypart1 160 0.5 0**

![image-20230213134503662](/Users/pasxsenger/Library/Application Support/typora-user-images/image-20230213134503662.png)

**Command 3: java Mypart1 360 0.5 0**

![image-20230213023228879](/Users/pasxsenger/Library/Application Support/typora-user-images/image-20230213023228879.png)



## Question 2

2. Let’s try another experiment, this time keep n (number of lines) constant and varying s (scale factor). Comment on your results by using various constant values of *n* for changing *s*. You may attach results, plot charts etc. to qualify your results.

**Command 1: java Mypart1 160 0.8 0**

 ![image-20230213023347989](/Users/pasxsenger/Library/Application Support/typora-user-images/image-20230213023347989.png)



**Command 2: java Mypart1 160 0.5 0**

![image-20230213023433711](/Users/pasxsenger/Library/Application Support/typora-user-images/image-20230213023433711.png)



**Command 3: java Mypart1 160 0.2 0**

![image-20230213023500372](/Users/pasxsenger/Library/Application Support/typora-user-images/image-20230213023500372.png)





## Conclusion

Through these two kinds of comparisons, we can conclude:

1. When the scale factor remains constant and the number of lines is allowed to vary, the aliasing effect increases with increasing the number of lines.
2. When the number of lines remains constant and the scale factor is allowed to vary, the aliasing effect increases with decreasing the scale factor.

To deal with aliasing, I applied low pass filter in the project. The result is shown below.

**Command: java Mypart1 160 0.5 1**

![image-20230213032048943](/Users/pasxsenger/Library/Application Support/typora-user-images/image-20230213032048943.png)





# **Analysis Questions for part 2**

## Question 1

If fps/s >= 2, os = s

else os = |1 - fps/s| * s

## Question 2

If s = 10 and fps = 25, os = 10

## Question 3

If s = 10 and fps = 16, os = 6

## Question 4

If s = 10 and fps = 10. os = 0

## Question 5

If s = 10 and fps = 8, os = 2
