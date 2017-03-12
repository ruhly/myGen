/**
  * @author truhland
  */
object MyGenerator {

  import ammonite.ops._
  import org.pegdown.ast._
  import org.pegdown.{Extensions, LinkRenderer, PegDownProcessor, ToHtmlSerializer}

  import collection.JavaConverters._
  import scalatags.Text.all._
  import MyPages._

  val postsFolder = pwd / 'post
  val targetFolder = pwd / 'site
  val assetsFolder = pwd / 'assets

  val assetFiles = ls ! assetsFolder
  val (markdownFiles, otherFiles) = ls ! postsFolder partition (_.ext == "md")
  markdownFiles.foreach(println)
  // Walk the posts/ folder and parse out the name, full- and first-paragraph-
  // HTML of each post to be used on their respective pages and on the index
  val posts = {
    val split = for (path <- markdownFiles) yield {
      val Array(number, name) = path.last.split(" - ", 2)
      (number, name.stripSuffix(".md"), path)
    }
    for ((index, name, path) <- split.sortBy(_._1.toInt)) yield {
      val processor = new PegDownProcessor(
        Extensions.FENCED_CODE_BLOCKS | Extensions.TABLES | Extensions.AUTOLINKS
      )
      val ast = processor.parseMarkdown(read(path).toArray)
      class Serializer extends ToHtmlSerializer(new LinkRenderer) {
        override def printImageTag(rendering: LinkRenderer.Rendering) {
          printer.print("<div style=\"text-align: center\"><img")
          printAttribute("src", rendering.href)
          // shouldn't include the alt attribute if its empty
          if (!rendering.text.equals("")) {
            printAttribute("alt", rendering.text)
          }
          import collection.JavaConverters._
          for (attr <- rendering.attributes.asScala) {
            printAttribute(attr.name, attr.value)
          }
          printer.print(" style=\"max-width: 100%; max-height: 500px\"")
          printer.print(" /></div>")
        }

        override def visit(node: HeaderNode) = {
          val tag = "h" + node.getLevel

          val id =
            node
              .getChildren
              .asScala
              .collect { case t: TextNode => t.getText }
              .mkString

          val setId = s"id=${'"' + sanitizeAnchor(id) + '"'}"
          printer.print(s"<$tag $setId>")
          visitChildren(node)
          printer.print(s"</$tag>")
        }

        override def visit(node: VerbatimNode) = {
          printer.println().print("<pre><code class=\"" + node.getType + "\">")

          var text = node.getText
          // print HTML breaks for all initial newlines
          while (text.charAt(0) == '\n') {
            printer.print("<br/>")
            text = text.substring(1)
          }
          printer.printEncoded(text)
          printer.print("</code></pre>")
        }

        override def visit(node: TableNode) = {
          currentTableNode = node
          printer.print("<table class=\"table table-bordered\">")
          visitChildren(node)
          printer.print("</table>")
          currentTableNode = null
        }
      }
      val rawHtmlContent = new Serializer().toHtml(ast)
      val snippetNodes =
        ast.getChildren
          .asScala
          .takeWhile {
            case n: SimpleNode if n.getType == SimpleNode.Type.HRule => false
            case _ => true
          }

      ast.getChildren.clear()
      snippetNodes.foreach(ast.getChildren.add)
      val pubDate = index.substring(4, 6) + '-' + index.substring(6, 8) +
        '-' + index.substring(0,4)
      val rawHtmlSnippet = new Serializer().toHtml(ast)
      (name, rawHtmlContent, rawHtmlSnippet, pubDate)
    }
  }

  val rssXml = {
    val snippet = tag("rss")(attr("version") := "2.0")(
      tag("channel")(
        tag("title")("Mostly Rajas"),
        tag("link")("http://www.truhland.net/blog"),
        tag("description")("a techblog"),
        tag("language")("en-us"),

        for ((name, _, rawHtmlSnippet, dateString) <- posts) yield tag("item")(
          tag("title")(name),
          tag("link")(s"http://www.truhland.net/blog/post/${sanitize(name)}.html"),
          tag("description")(rawHtmlSnippet),
          tag("pubDate")(dateString)
        )

      )
    )
    """<?xml version="1.0" encoding="UTF-8" ?>""" + snippet.render
  }

  //@main
  //def main(publish: Boolean = false): Unit = {
  // not running as script
  def main(args: Array[String]): Unit = {
    //val publish = false // don't automate publishing for starters

    rm ! targetFolder

    write(
      targetFolder / s"index.html",
      mainContent(posts)
    )

    mkdir ! targetFolder / 'post
    for (otherFile <- otherFiles) {
      cp(otherFile, targetFolder / 'post / (otherFile relativeTo postsFolder))
    }

    mkdir ! targetFolder / 'assets
    for (assetFile <- assetFiles) {
      cp(assetFile, targetFolder / 'assets / (assetFile relativeTo assetsFolder))
    }

    cp(pwd / "favicon.png", targetFolder / "favicon.ico")

    for ((name, rawHtmlContent, _, date) <- posts) {
      write(
        targetFolder / 'post / s"${sanitize(name)}.html",
        postContent(name, rawHtmlContent, date)
      )
    }

    write(targetFolder / "feed.xml", rssXml)

    // not publishing automatically
    // if (publish) { }
  }
}
