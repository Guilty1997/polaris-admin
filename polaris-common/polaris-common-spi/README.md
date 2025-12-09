# polaris-common-spi

## 模块作用
- 定义系统对外/对内的 SPI 扩展接口契约，不包含具体实现。
- 通过接口解耦业务模块与实现模块，方便按需装配或替换实现。
- 为三方插件或子系统提供稳定的扩展点，避免直接依赖核心业务代码。

## 使用方式
1. 在需要扩展的模块中引入依赖：
   ```xml
   <dependency>
       <groupId>com.polaris</groupId>
       <artifactId>polaris-common-spi</artifactId>
   </dependency>
   ```
2. 在 `polaris-common-spi` 中声明接口（如消息发送、审计上报等）。
3. 其他模块实现这些接口，并通过 Spring 容器或 SPI 机制注入/发现。
4. 业务代码仅依赖接口，不关心具体实现，便于替换与测试。

## 适用场景示例
- 多渠道实现（短信/邮件/IM）统一的发送接口。
- 多租户或 SaaS 场景下按租户选择不同实现。
- 将外部系统对接封装为可插拔的适配器。

