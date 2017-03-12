/**
  * @author truhland
  */
object MyPages {

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
                    li(a(href := s"$unNesting/post/HelloWorldBlog.html", "About"))
                  )
              ) // navbar-header
            ) // container
          ), // navbar
          div(cls := "container",
            div(cls := "row",
              div(cls := "col-md-9",
                tags2.section(id := "content",
                  div(cls := "page-header",
                    h1(pageTitle),
                    if (titleText.isEmpty) h3(cls := "small", "techblog")
                    else ""
                  ), // page header
                  contents
                ) // content section
              ) // col-md-9
            ) // row
          ), // container
          footer(
            div(cls := "container",
              hr(),
              div(cls := "row",
                div(cls := "col-xs-10", raw("&copy; 2017 Tim Ruhland "),
                    raw("&middot; built with my site generator using "),
                    a(href := "https://github.com/lihaoyi/blog",
                      target := "_blank", "Haoyi's Blog"), ", ",
                    a(href := "http://www.lihaoyi.com/Ammonite",
                      target := "_blank", "Ammonite"), ", ",
                    a(href := "http://www.lihaoyi.com/scalatags",
                      target := "_blank", "ScalaTags"), " and ",
                    a(href := "http://bootswatch.com",
                      target := "_blank", "Bootswatch")
                ),
                div(cls := "col-xs-2", p(cls := "pull-right",
                    i(cls := "fa fa-arrow-up"), a(href := "#", "Back to top"))
                )
              ) // row div
            ) // container div
          ) // footer
        ) // wrap div
      ) // body
    ).render
  }

  val currentTimeText = LocalDate.now.toString

  def forceHttps: Frag = script(raw(
    """if (window.location.protocol == "https:")
      |    window.location.href = "http:" + window.location.href.substring(window.location.protocol.length);
    """.
      stripMargin
  ))

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
                i(cls := "fa fa-calendar"), s" Posted $date"
              )
            ) // .post-info
          ),
          div(cls := "summary",
            p(raw(rawHtmlSnippet)),
            a(cls := "btn btn-default btn-xs", href := url, "more ...")
          )
        )
      },
      hr()
    )
  )

  def postContent(name: String, rawHtmlContent: String, date: String) = pageChrome(
    Some(name),
    "..",
    Seq[Frag](
      tags2.section(id := "content", cls := "body",
        span(cls := "published",
          i(cls := "fa fa-calendar"), s" Posted $date"
        ),
        div(cls := "entry-content",
          raw(rawHtmlContent)
          // commentBox(name)
        )
      )
    )
  )

}
