package nia.chapter2.echoserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author joseph
 * @date 2023/04/21
 * @description
 **/
public class HelloServer {
    public static void main(String[] args) {
        // 1、服务器端的启动器，负责装配下方的netty组件，启动服务器
        new ServerBootstrap()
                // 2、创建 NioEventLoopGroup，可以简单理解为 线程池 + Selector
                .group(new NioEventLoopGroup())
                // 3、选择服务器的 ServerSocketChannel 实现
                .channel(NioServerSocketChannel.class)
                // 4、child(work) 负责处理读写，该方法决定了 child(work) 执行哪些操作(handler)
                // ChannelInitializer 处理器（仅执行一次）
                // 5、channel的作用是待客户端SocketChannel建立连接后与客户端进行读写的通道，执行initChannel初始化，作用是添加别的handler
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {//添加handler
                        // 6、添加具体的handler
                        nioSocketChannel.pipeline().addLast(new StringDecoder());//使用StringDecoder解码，ByteBuf=>String

                        nioSocketChannel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {// 自定义handler，使用上一个处理器的处理结果
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception 							{
                                System.out.println(s);//打印上一步转换好的字符串
                            }
                        });
                    }
                    // 7、ServerSocketChannel绑定8080端口
                }).bind(8080);
    }
}

