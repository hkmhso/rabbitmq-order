# rabbitmq-order

> <font size=4>友情提示：请用Markdown编辑器打开。例如有道云笔记，CSDN。

<font size=4>主题：使用RabbitMQ模拟用户下单，外卖小哥抢单的情景。

<font size=4>1、项目搭建：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201104205824458.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ1NTc0MTgw,size_16,color_FFFFFF,t_70#pic_center)
<br>
<font size=4>2、架构图：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201105181940337.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ1NTc0MTgw,size_16,color_FFFFFF,t_70#pic_center)


<font size=4>3、pom文件：

```java
		<!-- 添加springboot对amqp的支持 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
```

<font size=4> 4、application配置文件：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201105173956452.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ1NTc0MTgw,size_16,color_FFFFFF,t_70#pic_center)
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020110517421342.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ1NTc0MTgw,size_16,color_FFFFFF,t_70#pic_center)

> <font size=4> PS: 连接上你的RabbitMQ和数据库

5、 表的设计：

订单表：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201104203924957.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ1NTc0MTgw,size_16,color_FFFFFF,t_70#pic_center)
> <font size=4>PS:一定要设置订单编号为<font size=3 color=red>唯一约束</font>，保证订单编号的唯一性。你也可以多添加一些字段，比如：下单时间，价格。。。。

外卖小哥表：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201104204129182.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ1NTc0MTgw,size_16,color_FFFFFF,t_70#pic_center)
> <font size=4>PS:  一定要将订单编号添加<font size=3 color=red>唯一约束</font>，这样就可以保证一个订单编号只能被一个外面小哥抢到。

 <font size=4> 6、启动消息生产者端，然后通过访问 `ip地址:端口号/api/placeOrder 模拟用户下单`

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201105175654277.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ1NTc0MTgw,size_16,color_FFFFFF,t_70#pic_center)

<font size=4> 7、`下完单到RabbitMQ的管理页面看看是否有消息存在于队列中`，如果，没有，请检查你的代码，配置文件，连接RabbitMQ是否成功等等情况。。

<font size=4>正确示例：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201105182153885.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ1NTc0MTgw,size_16,color_FFFFFF,t_70#pic_center)

<font size=4> 8、启动消息消费者端，看看控制器是否补单成功，是否只有一个外卖小哥抢到订单。

<font size=4>  正确示例：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201105180812291.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ1NTc0MTgw,size_16,color_FFFFFF,t_70#pic_center)

<font size=4> 9、至此，项目结束，谢谢你的支持。。。。
