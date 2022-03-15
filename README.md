# README

## 快速开始

- 通过 HashiCorp Vault 下载并安装好需要运行的 `vault`
- 打开 `vault-server`，使用命令 `vault server -config=config.hcl` 启动 vault
- 打开网页 `https://localhost:8200`，然后使用 `vault-cluster-vault-2022-03-12T05_31_07.406Z.json` 中 `keys_base64` 任意 3 个对 vault 进行解封，最后输入 `root_token` 进入 vault
  - 注意，在解封后输入  `root_token` 时偶尔会报出验证失败的提示，这时候刷新页面重新输入 `root_token` 即可
- 启动 `com.tjwoods.web.vault.client.WebApplication`，springboot 项目正常启动表示已经成功连接到 vault，并且 vault 中的数据可以在 springboot 中注入使用

## 项目说明

- `vault-server/bash` 文件夹其实是 [`spring-cloud-vault` 官方项目中的测试脚本](https://github.com/spring-cloud/spring-cloud-vault/tree/main/src/test/bash)，
其中调用 `create_certificates.sh` 可以直接生成 IP 为 `127.0.0.1` 的整套证书，方便我们在本地时直接测试 SSL 调用 vault
- `vault-server/vault` 则是 vault 启动时，config.hcl 所指定的数据保存地方
- 项目最外层的 `pom.xml` 中引用了 `spring-cloud-starter-vault-config` 依赖。如果是直接引用了 `spring-cloud-vault-config` 依赖的，那么当你需要以 SSL 连接 vault 时，需要额外提供 http 工具类，如 okhttp、httpclient 等，否则无法运行，会报错。
- 项目最后发现 web/webflux 与 vault 连接未见差别，但 spring-cloud-vault 官方文档有关于响应时的特别支持

## HashiCorp Vault 说明

- 秘密引擎（Secrets Engines）：刚进入 vault 看到的其实是 `Secrets Engines` 的页面，我们可以自己添加一个秘密引擎。最常用的就是 `KV`，也就是键值对类型的秘密数据
  - 秘密引擎相当于需要储存什么类型的秘密数据
  - 秘密引擎也是有名字的，这个名字将会成为秘密数据的路径的一部分
- 秘密数据 （Secrets）：创建好秘密引擎后，我们就可以在里面添加自己的秘密数据
  - 添加时，需要添加秘密数据的路径（path），这个路径将会与秘密引擎的名字结合起来，组成完成的路径
  - 当添加成功后，又想再添加，其实是为这个路径下的秘密数据更新版本
- 访问权限（Access）： 其中的 `Authentication Methods` 是用来配置各个秘密数据的路径的认证方式的
  - `TLS Certificates` 就是我们需要的证书认证
  - 添加好秘密数据后，需要为新的秘密数据的路径（如果有）指定认证方式
    - 我尝试过配置根路径（秘密引擎的名字）来包含其下所有路径的认证方式，但是无效，我是逐个配置的
- 策略（Policies）：这个其实是用来控制哪个路径能不能读取、修改、写入等操作的
  - 添加好秘密数据后，还需要在这里修改一下策略，让新的秘密数据路径可以被读取等，否则会报出 403 的异常
  - 这里是允许使用根路径（秘密引擎的名字）来包含其下所有路径的策略的，如添加以下策略，即表示允许 secret 的路径下所有的秘密数据被读取：
  ```
  path "secret/*" {
    capabilities = ["read"]
  }
  ```
  