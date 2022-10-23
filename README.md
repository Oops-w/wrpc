# Wrpc设计

## 协议

参考dubbo的协议设计

* Magic 4byte(32bit) 

  magic 魔术标识

* Version 8byte

  版本号

* Req/Res 1bit

  标识请求是request/response request = 1 response = 0

* 2 Way 1bit

  标识是否需要返回值 需要 = 1 不需要 = 0 仅当Req/Res = 1的时候有效

* Event 1bit

  判断是否是心跳事件 如果是心跳事件 = 1 不是的话= 0

* Serialization 5bit

  序列化的方式

* Status 8bit

  状态码 仅当Req/res = 0的时候有效

* Request ID 64bit

  唯一标识请求 long类型

* 数据长度 32bit

  发送的数据长度 int类型

在这个协议中 Req/Res 2Way Event Serialization 是1个byte

> bayte 11111111
>
> Req/Res = 0x80
>
> 2Way = 0x40
>
> Event = 0x20
>
> Version = 0x10
>
> Serialization = 0x1f

在decode的时候获取byte 然后去与以上的数据进行按位与操作

在encode的时候创建一个0x00的byte 然后通过按位或将值写入进去

