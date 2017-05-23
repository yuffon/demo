# Demo of RGSE

This project is about the demonstration of RGSE.

RGSE is a regular property guided dynamic symbolic execution (DSE) engine for finding the program paths satisfying a regular property. The technique implemented by RGSE was presented in [1].

Features
  (1). Effectiveness: RGSE can analyze real-world open source Java programs. 
  (2). Efficiency: RGSE can find the program paths satisfying a regular property as soon as possible through evaluating the history and future information of branches.
  (3). Detailed statistics: the iteration and time-consumption for finding the first accepted path, the accepted event sequence, and the inputs that can trigger the accepted path.  
  (4). Extensive applications: RGSE can be used for typestate bug finding, path-oriented test case generation, and performance tuning.      

The file [manuel.pdf](https://github.com/jrgse/demo/raw/master/manuel.pdf) introduces how to run the test scripts.

The docker image of RGSE can be downloaded from [this link](https://1drv.ms/u/s!Amd07GCbYt_zbQZm2w2MBbXI6Zo).

[1]. Yufeng Zhang, Zhenbang Chen, Ji Wang, Wei Dong, Zhiming Liu. Regular Property Guided Dynamic Symbolic Execution, in 37th IEEE/ACM International Conference on Software Engineering (ICSE 2015), IEEE Press, pp: 643-653. ([PDF](http://zbchen.github.io/Papers_files/icse2015.pdf))
