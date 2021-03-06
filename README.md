RGSE
===============================================
![rgse](https://raw.githubusercontent.com/srv4j/images/master/rgse.jpg)

RGSE is a regular property guided dynamic symbolic execution (DSE) engine for finding the program paths satisfying a regular property. The technique implemented by RGSE was presented in [1].

----------------- 

* [**Website of RGSE**](https://jrgse.github.io/)

--------------

# **Features**

  * `Good usability`: RGSE can analyze real-world open source Java programs with respect to not only single object but also multiple objects involved regular properties. 
  
  * `Effectiveness and efficiency`: RGSE can effectively find the program paths satisfying a regular property as soon as possible. 
  We applied RGSE to analyze 16 real-world open source Java programs against representative regular properties, compared with DFS 
  mode and pure path slicing mode, RGSE achieves an average 179x and 130x time speedups, respectively.
  
  * `Extensive applications`: typestate bug finding, path-oriented test case generation, performance tuning, and so on.    

----------  
# **Contacts**
	(1). Hengbiao Yu : hengbiaoyu@nudt.edu.cn
	(2). Yufeng Zhang : yuffonzhang@163.com
	(3). Zhenbang Chen : zbchen@nudt.edu.cn

----------  

[1]. Yufeng Zhang, Zhenbang Chen, Ji Wang, Wei Dong, Zhiming Liu. Regular Property Guided Dynamic Symbolic Execution, in 37th IEEE/ACM International Conference on Software Engineering (ICSE 2015), IEEE Press, pp: 643-653. ([PDF](http://zbchen.github.io/Papers_files/icse2015.pdf))
