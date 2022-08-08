# README

本文件夹中存放的是开发/测试环境所使用的 HashiCorpVault 环境。其中包含着 Vault 的数据，直接启动后即可使用。

## 快速启动

1. 请在终端中打开本文件夹的路径。此时终端的路径应该为 `xxx/vault-server`
2. 执行 `start.sh` 脚本，启动 vault 并自动解封。脚本的唯一参数是 HashiCorpVault 的 vault 程序（自行到 HashiCorpVault 官网下载）路径。
例如 vault 程序的路径为 `/home/user/vault/vault`，那么执行脚本的命令为：
```shell
./start.sh /home/user/vault/vault
```

注意：正常情况下安装 vault 后是可以直接使用 vault 命令的，但远程桌面各用户无法安装 vault，所以直接使用全路径的方式调用