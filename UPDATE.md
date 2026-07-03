## 更新日志

### 1.1.9（2025-11-20）

  1. 删除TODO.md文件；
  2. 修改pom.xml文件：
    - 新增依赖commons-io:commons-io:2.14.0；
    - 删除依赖org.apache.maven.shared:maven-invoker:3.2.0；
    - 将部分依赖由provided调整为optional；
  3. 删除还未实现的DateTime工具；
  4. Regex预定义正则字符转义工具方法（未实现）；
  5. IO替换已被废弃的API方法（commons-io）；
  6. 删除还未实现的io.fileWatch包；
  7. 优化Properties及PropertyUtil；
  8. 删除ServiceLoader类（已转移至goblin-spi项目）；
  9. 新增文件/目录复制、移动及重命名工具：
    - 新增FileCopier工具；
    - Resources新增文件/目录复制、移动及重命名便捷静态方法；
    - Resources修复若干问题，以及添加部分API注释；
  10. 线程池执行器实现（ThreadPoolExecutor）添加Runnable任务队列等待中断机制；


### 1.1.10（2026-04-09）

    1. 修复Resources#load工具加载文件输入流失败未自动释放问题；
    2. 修复Resources#deleteIfExists方法在遍历目录Path失败时未自动释放目录句柄问题；


### 1.1.11 （2026-05-12）

    1. 新增Classes#getSimpleClassName方法；
    2. 优化Resources#readByte、Resources#readString方法，默认提供4096长度缓冲区；
    3. 优化MethodReference，提供异常自定义处理能力，并新增四、五、六个参数方法调用能力；
    4. 优化Reflections中通过lambda方法引用实例获取方法信息的相关实现；
    5. 修复Reflections#findGenericParameterType方法代码缺失问题；


### 1.2.0（2026-06-22）
    
    1. 重构Throws工具： 
        - 实现由包装异常改为类型查除; 
        - 新增部分工具，删除包装异常相关方法；
    2. 重构requireXXX相关断言方法：同Throws实现改为类型查除并调整堆栈信息；
    3. 新增URI、URL相关协议判断、类型互转相关方法；
    4. 优化zip/unzip工具，提供解压输入流能力；
    5. 新增获取代理类原始类型工具方法；


### 1.2.1（2026-06-23）
    
    1. 修复Unzip工具EOFException问题；
    
    
### 1.3.0（2026-07-03）

    1. 新增移除List指定元素工具方法：Objects#removeValue、Objects#removeNullValue；
    
    
    