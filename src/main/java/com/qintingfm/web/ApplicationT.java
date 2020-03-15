package com.qintingfm.web;

import com.qintingfm.web.service.HtmlService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 应用程序启动类
 *
 * @author guliuzhong
 */

public class ApplicationT {
    public static void main(String[] args) {
        String h="<p>Java 14 计划于 3 月 17 号发布。这一版本包含的 JEP 比 Java 12 和 Java 13 的总和还要多。那么，对于每天需要面对 Java 代码的开发者来说，哪些东西最值得关注？</p>\n" +
                "<p>本文将着重介绍以下这些 Java 新特性：</p>\n" +
                "<p>改进的 switch 表达式。这一特性已经作为预览版出现在 Java 12 和 Java 13 中，而 Java 14 将带来它的完整正式版。</p>\n" +
                "<p>instanceof 的模式匹配（这是个一语言特性）。</p>\n" +
                "<p>非常有用的 NullPointerException 信息（这是一个 JVM 特性）。</p>\n" +
                "<p>switch 表达式<br>\n" +
                "在 Java 14 中，switch 表达式是一个正式的特性。而在之前的两个 Java 版本中，这个特性只是预览版。设定“预览版”的目的是为了收集开发者反馈，并根据反馈结果决定相应的特性是否要做出修改，甚至是移除，但其中的大部分都会成为正式特性。</p>\n" +
                "<p>新的 switch 表达式有助于减少 bug，因为它的表达和组合方式更容易编写。例如，下面的示例使用了箭头语法：</p>\n" +
                "<p><br>\n" +
                "var log = switch (event) {<br>&nbsp;&nbsp;&nbsp;&nbsp; case PLAY -&gt; \"User has triggered the play button\";<br>&nbsp;&nbsp;&nbsp;&nbsp; case STOP, PAUSE -&gt; \"User needs a break\";<br>&nbsp;&nbsp;&nbsp;&nbsp; default -&gt; {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; String message = event.toString();<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; LocalDateTime now = LocalDateTime.now();<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; yield \"Unknown event \" + message + <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \" logged on \" + now;<br>&nbsp;&nbsp;&nbsp;&nbsp; }<br>\n" +
                "};<br>\n" +
                "文本块<br>\n" +
                "Java 13 引入了文本块特性，并将其作为预览版。有了这个特性，处理多行字符串字面量就容易了很多。在 Java 14 中，该特性仍然是预览版，不过做了一些调整。在没有这个特性之前，要表示多行格式化的字符串需要像下面这样：</p>\n" +
                "<p><br>\n" +
                "String html = \"&lt;HTML&gt;\" +<br>\n" +
                "\"\\n\\t\" + \"&lt;BODY&gt;\" +<br>\n" +
                "\"\\n\\t\\t\" + \"&lt;H1&gt;\\\"Java 14 is here!\\\"&lt;/H1&gt;\" +<br>\n" +
                "\"\\n\\t\" + \"&lt;/BODY&gt;\" +<br>\n" +
                "\"\\n\" + \"&lt;/HTML&gt;\";<br>\n" +
                "有了文本块特性之后，可以使用三引号来表示字符串的开头和结尾，这样的代码看起来更简洁、更优雅：</p>\n" +
                "<p><br>\n" +
                "String html = \"\"\"<br>\n" +
                "&lt;HTML&gt;<br>&nbsp;&nbsp; &lt;BODY&gt;<br>&nbsp;&nbsp;&nbsp;&nbsp; &lt;H1&gt;\"Java 14 is here!\"&lt;/H1&gt;<br>&nbsp;&nbsp; &lt;/BODY&gt;<br>\n" +
                "&lt;/HTML&gt;\"\"\";<br>\n" +
                "在 Java 14 中，该特性增加了两个转义字符。一个是\\s，用来表示单空格。一个是反斜杠\\，用在行末表示不换行。如果你有一个很长的字符串，为了让代码看起来更好看，但又不希望真的换行，就可以使用这个转义字符。</p>\n" +
                "<p>例如，目前的多行字符串是这样的：</p>\n" +
                "<p><br>\n" +
                "String literal = <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \"Lorem ipsum dolor sit amet, consectetur adipiscing \" +<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \"elit, sed do eiusmod tempor incididunt ut labore \" +<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \"et dolore magna aliqua.\";<br>\n" +
                "使用了新的转义字符之后是这样的：</p>\n" +
                "<p>\uE676复制代码<br>\n" +
                "String text = \"\"\"<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Lorem ipsum dolor sit amet, consectetur adipiscing \\<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; elit, sed do eiusmod tempor incididunt ut labore \\<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; et dolore magna aliqua.\\<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \"\"\";<br>\n" +
                "instanceof 的模式匹配<br>\n" +
                "为了避免在使用 instanceof 后还需要进行类型转换，Java 14 引入了一个新的预览版特性。例如，在没有该特性之前：</p>\n" +
                "<p><br>\n" +
                "if (obj instanceof Group) {<br>&nbsp;&nbsp; Group group = (Group) obj;<br>&nbsp;&nbsp; // 调用 group 的方法<br>&nbsp;&nbsp; var entries = group.getEntries();<br>\n" +
                "}<br>\n" +
                "我们可以使用新的特性来重写这段代码：</p>\n" +
                "<p><br>\n" +
                "if (obj instanceof Group group) {<br>&nbsp;&nbsp; var entries = group.getEntries();<br>\n" +
                "}<br>\n" +
                "既然条件检查已经确认 obj 是 Group 类型，那为什么还要再次进行显式的类型转换呢？这样有可能更容易出错。新的语法可以将代码中的大部分类型转换移除掉。2011 年发布的一份研究报告显示，Java 代码中有 24% 的类型转换是跟在 instanceof 之后的。</p>\n" +
                "<p>Joshua Bloch 的经典著作《Effective Java》中有一段代码示例：</p>\n" +
                "<p><br>\n" +
                "@Override public boolean equals(Object o) { <br>&nbsp;&nbsp;&nbsp;&nbsp; return (o instanceof CaseInsensitiveString) &amp;&amp; <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ((CaseInsensitiveString) o).s.equalsIgnoreCase(s); <br>\n" +
                "}<br>\n" +
                "这段代码可以使用新的语法写成：</p>\n" +
                "<p><br>\n" +
                "@Override public boolean equals(Object o) { <br>&nbsp;&nbsp;&nbsp;&nbsp; return (o instanceof CaseInsensitiveString cis) &amp;&amp;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; cis.s.equalsIgnoreCase(s); <br>\n" +
                "}<br>\n" +
                "这个特性很有意思，因为它为更为通用的模式匹配打开了大门。模式匹配通过更为简便的语法基于一定的条件来抽取对象的组件，而 instanceof 刚好是这种情况，它先检查对象类型，然后再调用对象的方法或访问对象的字段。</p>\n" +
                "<p>记录类（Record）<br>\n" +
                "另一个预览特性是“记录”。该特性主要是为了降低 Java 语法的“啰嗦”程度，让开发者写出更简洁的代码。这个特性主要用在某些领域类上，这些类主要用于保存数据，不提供领域行为。</p>\n" +
                "<p>我们以一个简单的领域类 BankTransaction 作为例子，它包含了三个字段：date、amount 和 description。目前，这个类需要以下几个组件：</p>\n" +
                "<p>构造器；</p>\n" +
                "<p>getter 方法；</p>\n" +
                "<p>toString() 方法；</p>\n" +
                "<p>hashCode() 和 equals() 方法。</p>\n" +
                "<p>这些方法一般可以通过 IDE 自动生成，但会占用很大的代码空间，例如：</p>\n" +
                "<p><br>\n" +
                "public class BankTransaction {<br>&nbsp;&nbsp;&nbsp;&nbsp; private final LocalDate date;<br>&nbsp;&nbsp;&nbsp;&nbsp; private final double amount;<br>&nbsp;&nbsp;&nbsp;&nbsp; private final String description;<br>&nbsp; <br>&nbsp; <br>&nbsp;&nbsp;&nbsp;&nbsp; public BankTransaction(final LocalDate date, <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; final double amount, <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; final String description) {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; this.date = date;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; this.amount = amount;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; this.description = description;<br>&nbsp;&nbsp;&nbsp;&nbsp; }<br>&nbsp; <br>&nbsp;&nbsp;&nbsp;&nbsp; public LocalDate date() {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; return date;<br>&nbsp;&nbsp;&nbsp;&nbsp; }<br>&nbsp; <br>&nbsp;&nbsp;&nbsp;&nbsp; public double amount() {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; return amount;<br>&nbsp;&nbsp;&nbsp;&nbsp; }<br>&nbsp; <br>&nbsp;&nbsp;&nbsp;&nbsp; public String description() {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; return description;<br>&nbsp;&nbsp;&nbsp;&nbsp; }<br>&nbsp; <br>&nbsp;&nbsp;&nbsp;&nbsp; @Override<br>&nbsp;&nbsp;&nbsp;&nbsp; public String toString() {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; return \"BankTransaction{\" +<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \"date=\" + date +<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \", amount=\" + amount +<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; \", description='\" + description + '\\'' +<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; '}';<br>&nbsp;&nbsp;&nbsp;&nbsp; }<br>&nbsp; <br>&nbsp;&nbsp;&nbsp;&nbsp; @Override<br>&nbsp;&nbsp;&nbsp;&nbsp; public boolean equals(Object o) {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if (this == o) return true;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if (o == null || getClass() != o.getClass()) return false;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; BankTransaction that = (BankTransaction) o;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; return Double.compare(that.amount, amount) == 0 &amp;&amp;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; date.equals(that.date) &amp;&amp;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; description.equals(that.description);<br>&nbsp;&nbsp;&nbsp;&nbsp; }<br>&nbsp; <br>&nbsp;&nbsp;&nbsp;&nbsp; @Override<br>&nbsp;&nbsp;&nbsp;&nbsp; public int hashCode() {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; return Objects.hash(date, amount, description);<br>&nbsp;&nbsp;&nbsp;&nbsp; }<br>\n" +
                "}<br>\n" +
                "Java 14 提供了一种方式，可以避免这种繁琐的代码，满足开发者希望一个类只是用来聚合数据的意图。BankTransaction 可以重构成：</p>\n" +
                "<p><br>\n" +
                "public record BankTransaction(LocalDate date,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; double amount,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; String description) {}<br>\n" +
                "除了构造器和 getter 方法，还会自动生成 equals、hashCode 和 toString 方法。</p>\n" +
                "<p>要尝试这个特性，需要在编译代码时打开预览标签：</p>\n" +
                "<p><br>\n" +
                "javac --enable-preview --release 14 BankTransaction.java<br>\n" +
                "记录类的字段隐式都是 final 的，也就是说不能对它们进行动态赋值。不过要注意，这并不意味着整个记录类对象都是不可变的，如果字段保存的是对象，那么这个对象是可变的。</p>\n" +
                "<p>这里要插一句话，如果从培训的角度来讲，例如你要教会初级开发者，那么记录类应该在什么时候讲授比较好？在介绍 OOP 和类的概念之前还是之后？</p>\n" +
                "<p>有用的 NullPointerException 信息<br>\n" +
                "有些人认为，抛出 NullPointerException 应该成为 Java 编程的一个新的“Hello world”，因为这是不可避免的。NullPointerException 确实让人抓狂，它们经常出现在生产环境的日志里，但调试起来很困难。例如，看看下面这段代码：</p>\n" +
                "<p><br>\n" +
                "var name = user.getLocation().getCity().getName();<br>\n" +
                "这段代码可能会抛出一个异常：</p>\n" +
                "<p><br>\n" +
                "Exception in thread \"main\" java.lang.NullPointerException<br>&nbsp;&nbsp;&nbsp;&nbsp; at NullPointerExample.main(NullPointerExample.java:5)<br>\n" +
                "在一行代码里连续调用了多个方法，比如 getLocation() 和 getCity()，它们都有可能返回 null，而 user 也可能为 null。所以，我们无法知道是什么导致了 NullPointerException。</p>\n" +
                "<p>在 Java 14 中，JVM 会抛出更多有用的诊断信息：</p>\n" +
                "<p><br>\n" +
                "Exception in thread \"main\" java.lang.NullPointerException: Cannot invoke \"Location.getCity()\" because the return value of \"User.getLocation()\" is null<br>&nbsp;&nbsp;&nbsp;&nbsp; at NullPointerExample.main(NullPointerExample.java:5)<br>\n" +
                "错误信息提供了两个内容：</p>\n" +
                "<p>结果：无法调用 Location.getCity()。</p>\n" +
                "<p>原因：User.getLocation() 返回值是 null。</p>\n" +
                "<p>要启用这个功能，需要添加 JVM 标识：</p>\n" +
                "<p><br>\n" +
                "-XX:+ShowCodeDetailsInExceptionMessages<br>\n" +
                "例如：</p>\n" +
                "<p><br>\n" +
                "java -XX:+ShowCodeDetailsInExceptionMessages NullPointerExample<br>\n" +
                "据报道，在未来的版本中，这个特性可能会默认启用。</p>\n" +
                "<p>这个增强特性不仅适用于方法调用，只要会导致 NullPointerException 的地方也都适用，包括字段的访问、数组的访问和赋值。</p>\n" +
                "<p>结论<br>\n" +
                "Java 14 带来了一些新的预览特性，例如可用于避免显式类型转换的 instanceof 模式匹配。它还引入了记录类，提供了一种简洁的方式来创建只用于聚合数据的类。另外，增强的 NullPointerException 错误信息有助于更好地进行诊断。switch 表达式成为 Java 14 的正式特性。文本块进入第二轮预览，新增了两个转义字符。</p><p>当然 目前 国内使用的主流版本依然是java8 ，部分应用使用java11.</p><p>&nbsp;</p><p>&nbsp;</p><p>qintingfm.com</p>";
        HtmlService htmlService=new HtmlService();
        System.out.println(htmlService.filterNone(h));;
        System.out.println("-------------------");;
        System.out.println(htmlService.filterNone(h));;
    }
}
