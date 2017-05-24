RGSE
![alt text](https://raw.githubusercontent.com/jrgse/images/master/FSM.jpg)
===============================================

RGSE is a regular property guided dynamic symbolic execution (DSE) engine for finding the program paths satisfying a regular property. The technique implemented by RGSE was presented in [1].

-----------------

* [**Home**](https://jrgse.github.io/demo/)  

* [**Tutorial**](https://jrgse.github.io)

* [**Manuel**](https://github.com/jrgse/demo/raw/master/manuel.pdf)

* [**Docker Image**](https://1drv.ms/u/s!Amd07GCbYt_zbQZm2w2MBbXI6Zo)

--------------

# **Features**
  * `Effectiveness`: RGSE can analyze real-world open source Java programs. 
  
  * `Efficiency`: RGSE can find the program paths satisfying a regular property as soon as possible through evaluating the history and future information of branches.
  
  * `Detailed statistics`: the iteration and time-consumption for finding the first accepted path, the accepted event sequence, the inputs that can trigger the accepted path, and so on.  
  
  * `Extensive applications`: typestate bug finding, path-oriented test case generation, performance tuning, and so on.    

----------  
# **Contacts**
	(1). Hengbiao Yu : hengbiaoyu@nudt.edu.cn
	(2). Zhenbang Chen : zbchen@nudt.edu.cn

----------  

[1]. Yufeng Zhang, Zhenbang Chen, Ji Wang, Wei Dong, Zhiming Liu. Regular Property Guided Dynamic Symbolic Execution, in 37th IEEE/ACM International Conference on Software Engineering (ICSE 2015), IEEE Press, pp: 643-653. ([PDF](http://zbchen.github.io/Papers_files/icse2015.pdf))
