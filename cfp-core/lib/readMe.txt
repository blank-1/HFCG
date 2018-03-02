将maven库中com下fuiou目录删掉
寻找到cfp-core项目路径绝对路径path,替换下面的path，执行以下命令，将fuiou_goldacnt_sdk-v0.31.jar添加到本地maven库中
mvn install:install-file -Dfile=path\lib\fuiou_goldacnt_sdk-v0.31.jar -DgroupId=com.fuiou -DartifactId=fuiou_goldacnt_sdk -Dversion=v0.31 -Dpackaging=jar

初始化xbean包 	path是项目下的lib目录的物理路径
mvn install:install-file -Dfile=path\lib\xbean.jar -DgroupId=com.hu -DartifactId=xbean -Dversion=2.x -Dpackaging=jar
