package nia.chapter2.echoclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author joseph
 * @date 2023/04/21
 * @description
 **/
public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        //1、启动类
        new Bootstrap()
                //2、添加EventLoop
                .group(new NioEventLoopGroup())
                // 3、选择客户 Socket 实现类，NioSocketChannel 表示基于 NIO 的客户端实现
                .channel(NioSocketChannel.class)
                // 4、添加处理器   ChannelInitializer 处理器（仅执行一次）
                // 它的作用是待客户端SocketChannel建立连接后，执行initChannel以便添加更多的处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {//初始化器会在连接建立后被调用，调用后就会执行下面的initChannel
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        // 消息会经过通道 handler 处理，这里是将 String => ByteBuf 编码发出
                        channel.pipeline().addLast(new StringEncoder());
                    }
                })
                // 指定要连接的服务器和端口
                .connect(new InetSocketAddress("localhost", 8080))
                // Netty 中很多方法都是异步的，如 connect
                // 这时需要使用 sync 方法等待 connect 建立连接完毕,是一个阻塞方法，知道连接建立
                .sync()
                // 获取 channel 对象，它即为通道抽象，可以进行数据读写操作
                .channel()
                // 写入消息并清空缓冲区，不管收发数据，都会走handle，调用处理器内部的方法
                .writeAndFlush("hello world");//把字符串转成了bytebuf
    }
}

