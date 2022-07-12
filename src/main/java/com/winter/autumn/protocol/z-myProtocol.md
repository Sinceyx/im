### this is my protocol


+ 魔数: 第一时间判断是否是无效数据包 BABY 4个字节
+ 版本号: 可以支持协议的升级 V1 2个字节
+ 序列化算法: 消息正文采用的序列化和发序列化方式，由此可以拓展：json，protobuf，hessian，jdk 1个字节
+ 指令类型： 主要还是和业务相关联 Integer  1个字节
+ 请求序号：为了双工通信，提升异步能力 4个zijie
+ 正文长度 Integer 4个字节
+ 消息正文