/**
  * @author truhland
  */
object MyPages {


  //import $ivy.`com.lihaoyi::scalatags:0.6.2`
  import java.time.LocalDate

  import scalatags.Text.all._
  import scalatags.Text.tags2
  //import $file.pageStyles, pageStyles._


  def sanitize(s: String): String = {
    s.filter(_.isLetterOrDigit)
  }

  def sanitizeAnchor(s: String): String = {
    s.split(" |-", -1).map(_.filter(_.isLetterOrDigit)).mkString("-").toLowerCase
  }

  def pageChrome(titleText: Option[String], unNesting: String, contents: Frag): String = {
    val pageTitle = titleText.getOrElse("Mostly Rajas")
    val sheets = Seq(
      "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.7/yeti/bootstrap.min.css",
      "https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css",
      "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.9.0/styles/github-gist.min.css",
      "http://www.truhland.net/blog/assets/css/style.css"
    )
    html(
      head(
        meta(charset := "utf-8"),
        meta(httpEquiv := "X-UA-Compatible", content := "IE=edge"),
        meta(name := "viewport", content := "initial-scale = 1.0,maximum-scale = 1.0"),
        for (sheet <- sheets)
          yield { link(href := sheet, rel := "stylesheet", `type` := "text/css") },
        tags2.title(pageTitle),
        script(src := "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.9.0/highlight.min.js"),
        script(src := "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.9.0/languages/scala.min.js"),
        script(raw("hljs.initHighlightingOnLoad();")),
        //googleAnalytics,
        forceHttps
      ),
      body(
        div(cls := "wrap",
          div(cls := "navbar navbar-default navbar-fixed-top", role := "navigation",
            div(cls := "container",
              div(cls := "navbar-header",
                a(href := "http://www.truhland.net", cls := "navbar-brand", "truhland.net"),
                  ul(cls := "nav navbar-nav",
                    li(a(href := "http://www.truhland.net/blog", "Blog")),
                    li(a(href := "pages/about.html", "About"))
                  )
              ) // navbar-header
            ) // container
          ), // navbar
          div(cls := "container",
            div(cls := "row",
              div(cls := "col-md-9",
                tags2.section(id := "content",
                  div(cls := "page-header",
                    titleText.map(h1(_)),
                    h3(cls := "small", "techblog")
                  ), // page header
                  contents
                ) // content section
              ) // col-md-9
            ) // row
          ) // container
        ) // wrap div
      ) // body
    ).render
  }

  val currentTimeText = LocalDate.now.toString

  def commentBox(titleText: String): Frag = Seq(
    div(id := "disqus_thread"),
    script(raw(
      s"""
      /**
      * RECOMMENDED CONFIGURATION VARIABLES: EDIT AND UNCOMMENT THE SECTION BELOW TO INSERT DYNAMIC VALUES FROM YOUR PLATFORM OR CMS.
      * LEARN WHY DEFINING THESE VARIABLES IS IMPORTANT: https://disqus.com/admin/universalcode/#configuration-variables
      */

      var disqus_config = function () {
      this.page.url = "http://www.lihaoyi.com/post/${sanitize(titleText)}.html"; // Replace PAGE_URL with your page's canonical URL variable
      this.page.identifier = "$titleText"; // Replace PAGE_IDENTIFIER with your page's unique identifier variable
      };

      (function() { // DON'T EDIT BELOW THIS LINE
      var d = document, s = d.createElement('script');

      s.src = '//lihaoyi.disqus.com/embed.js';

      s.setAttribute('data-timestamp', +new Date());
      (d.head || d.body).appendChild(s);
      })();
  """))
  )

  def googleAnalytics: Frag = script(raw(
    """(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
    |(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
    |m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
    |})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
    |
    |ga('create', 'UA-27464920-5', 'auto');
    |ga('send', 'pageview');
  """.
      stripMargin
  ))

  def forceHttps: Frag = script(raw(
    """if (window.location.protocol == "https:")
      |    window.location.href = "http:" + window.location.href.substring(window.location.protocol.length);
    """.
      stripMargin
  ))

  def metadata(date: String) = div(
    color := "#999",
    marginBottom := 20,
    "Posted ", date
  )

  def mainContent(posts: Seq[(String, String, String, String)]) = pageChrome(
    None
    ,
    ".",
    div(
      for((name, _, rawHtmlSnippet, date) <- posts.reverse) yield {
        val url = s"post/${sanitize(name)}.html"
        tags2.article(
          h2(a(href := url, name)),
          div(cls := "well well-sm",
            footer(cls := "post-info",
              span(cls := "label label-default", "Date"),
              span(cls := "published",
                i(cls := "fa fa-calendar"), date
              )
            ) // .post-info
          ),
          div(cls := "summary",
            p(raw(rawHtmlSnippet)),
            a(cls := "btn btn-default btn-xs", href := url, "more ...")
          )
        ), // article
        hr()
      }
    )
  )

  def postContent(name: String, rawHtmlContent: String, date: String) = pageChrome(
    Some(name),
    "..",
    Seq[Frag](
      metadata(date),
      raw(rawHtmlContent),
      // commentBox(name)
    )
  )

}
