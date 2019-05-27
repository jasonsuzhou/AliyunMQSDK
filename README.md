# 支持高并发可配置的阿里云消息队列开发SDK

## 配置文件详解

```json
{
  "accessKey":"这个阿里云消息队列后台可见",
  "secretKey":"这个阿里云消息队列后台可见",
  "accessUrl":"http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet",/*请求地址*/
  "logPath":"/mnt/logs/aliyun/",/*阿里云提供的jar包所打印的log目录*/
  "logFileMaxIndex":10,/*最大保留的log文件数目，默认一个文件是64M*/
  "logLevel":"WARN",/*打印log的级别，ERROR WARN INGO DEBUG*/
  "topics":[
    {"id":"自定义ID，确保唯一",
    "name":"这个是对应阿里云消息队列的TOPIC名称",
    "producer":{"id":"自定义ID，确保唯一",
    				"name":"这个是对应阿里云消息队列的PRODUCER名称，通常是PID_开头",
    				"retryTimes":3,/*发送消息失败后重发的次数配置*/
    				"sendMsgTimeoutMillis":10000/*发送消息的超时时间设置，单位为毫秒*/
    				},
    "consumers":{"id":"自定义ID，确保唯一",
    				 "name":"这个是对应阿里云消息队列的CONSUMER名称，通常是CID_开头",
    				 "tag":"*"/*匹配消息标签获取对应的消息*/
    				 }
    }
  ],
  "listeners":[/*监听器配置，可以多个*/
    {"id":"自定义ID，确保唯一",
    "consumerRef":"对应监听哪个CONSUMER,这里填consumers配置的id值，不是name值",
    "initThreads":2,/*初始化多少个监听器线程*/
    "initStatus":"start",/*监听器初始化状态，是启动(start)还是关闭(stop)*/
    "serviceClass":"com.mh.aliyun.mq.sdk.processor.DemoProcessor",/*处理消息的实现类*/
    	"executionPool":/*监听器线程的里处理消息的任务线程池配置*/
    		{"minSize":5,/*初始化线程池大小*/
    		 "waitSize":1,/*等待线程大小*/
    		 "maxSize":20,/*最大线程大小*/
    		 "keepAliveTime":1000/*线程活跃时间*/
    		}
    }
  ]
}
```
## 使用向导

- 继承如下java类,实现process方法就可以了，这个方法的message参数就是从阿里云消息队列获得的消息

```java
com.mh.aliyun.mq.sdk.processor.AbstractMessageProcessor

public class DemoProcessor extends AbstractMessageProcessor {

	public DemoProcessor(Message message) {
		super(message);
	}

	@Override
	public void beforeProcess() throws Exception {
		System.out.println("invoked beforeProcess......");
	}

	@Override
	public void afterProcess() throws Exception {
		System.out.println("invoked afterProcess......");
	}

	@Override
	protected void process(String message) {
		System.out.println("received message::"+message);
	}
}
```
- 写好实现类就可以把该类的地址配置到配置文件中的serviceClass了，对应的监听器获得的消息都会交给这个处理类处理
- 初始化配置文件

```java
File file = new File("你的配置文件地址");
com.mh.aliyun.mq.sdk.config.AliyunMQConfigReader.initConfig(file);
```
- 初始化SDK运行时环境,初始化成功就表明监听器开始工作了

```java
com.mh.aliyun.mq.sdk.setup.AliyunMQManager.begin();
```
- 关闭SDK运行时环境,应用程序关闭的时候可以调用该方法

```java
com.mh.aliyun.mq.sdk.setup.AliyunMQManager.end();
```
- 发送消息是通过producer发送的，所以可以通过如下方法发送

```java
com.mh.aliyun.mq.sdk.setup.AliyunMQManager.getSender("配置的producer id").sendMessage("消息内容");

```

- 更多操作请参考如下类的注释吧

```java
com.mh.aliyun.mq.sdk.setup.AliyunMQManager
```

## Sample

```java
	public static void main(String[] args) throws Exception {
		File file = new File("./src/demo_config.json");
		AliyunMQConfigReader.initConfig(file);
		AliyunMQManager.begin();
		
		//如果配置配置默认监听器初始化的时候不启动，那么后续我们可以通过代码手动启动
		AliyunMQManager.startListener("L003");
		//手动停止监听器的所有线程
		AliyunMQManager.stopListener("L003");
		//除了配置文件配置的线程数量，还可以动态添加线程数量
		String threadId = AliyunMQManager.addOneListenerThread("L003");
		//根据线程ID动态减少线程数量
		AliyunMQManager.stopListenerThread(threadId);
		//获得一个sender发送消息到阿里云消息队列
		AliyunMQMsgSenderFactory.getSender("PID_TEST").sendMessage("Hello World");
		
		AliyunMQManager.end();
	}
```

## TODO
- 可以单独暂停某个监听器线程不去接收消息

## Update History
- 2018-06-01 利用apache commons pool2提供的组件实现了任务池
- 2019-05-28 适配新接口，原阿里云消息队列没有了，取而代之的值阿里云RocketMQ

## Note
- 2018-06-27 16:27 到 2018-06-27 17:40 左右阿里云消息队列宕机了