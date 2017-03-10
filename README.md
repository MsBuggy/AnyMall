# AnyMall
A Simple B2C E-commerce project written in Java.

## 环境准备
- Maven
- Git
- TortoiseGit(Windows), SourceTree(Mac OS)
- Java IDE工具，Eclipse, MyEclipse等，必须有Maven插件。

## 下载代码
1. 使用git命令行checkout代码。
	```
		 git clone https://github.com/MsBuggy/AnyMall.git
	```
2. 使用GUI工具TortoiseGit。直接在Windows某个文件夹里，右键，选择 “Git Clone...”菜单，填入仓库地址：
	```
		https://github.com/MsBuggy/AnyMall.git
	```

## 导入项目到Eclipse
1. 打开File->Import...菜单。
2. 选择Maven->Existing Maven Projects...
3. 选择项目目录（选到含有pom.xml的目录）

## Build项目
项目右键 Run as->maven clean，然后 maven install.
