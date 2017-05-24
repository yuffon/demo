Demo of RGSE
===============================================
[Home](https://jrgse.github.io/demo/)  [Tutorial](https://jrgse.github.io)
![alt text](https://github.com/jrgse/images/blob/master/FSM.jpg) 
![image](http://cn.bing.com/images/search?view=detailV2&ccid=pyrfYkeW&id=DD4FB92516F9BF5834E8E4538425102608E5A32F&thid=OIP.pyrfYkeWs7-u93U5pMaYGgEsEQ&q=Finite+state+machine&simid=608053983473044237&selectedIndex=4&ajaxhist=0)

RGSE is a regular property guided dynamic symbolic execution (DSE) engine for finding the program paths satisfying a regular property. The technique implemented by RGSE was presented in [1].

--------------

# **Features**
  * `Effectiveness`: RGSE can analyze real-world open source Java programs. 
  
  * `Efficiency`: RGSE can find the program paths satisfying a regular property as soon as possible through evaluating the history and future information of branches.
  
  * `Detailed statistics`: the iteration and time-consumption for finding the first accepted path, the accepted event sequence, the inputs that can trigger the accepted path, and so on.  
  
  * `Extensive applications`: typestate bug finding, path-oriented test case generation, performance tuning, and so on.    

----------  


The file [manuel.pdf](https://github.com/jrgse/demo/raw/master/manuel.pdf) introduces how to run the test scripts.

The docker image of RGSE can be downloaded from [`this link`](https://1drv.ms/u/s!Amd07GCbYt_zbQZm2w2MBbXI6Zo).

[1]. Yufeng Zhang, Zhenbang Chen, Ji Wang, Wei Dong, Zhiming Liu. Regular Property Guided Dynamic Symbolic Execution, in 37th IEEE/ACM International Conference on Software Engineering (ICSE 2015), IEEE Press, pp: 643-653. ([PDF](http://zbchen.github.io/Papers_files/icse2015.pdf))
