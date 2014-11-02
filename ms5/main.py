from google.appengine.api import users
# from gaesessions import Session, get_current_session, set_current_session

import cgi
import webapp2
import Cookie

main_html = """\
<!DOCTYPE html>
<html lang="en">
<head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Magic: The Gathering Card Tracker</title>

        <!-- Bootstrap Core CSS - Uses Bootswatch Flatly Theme: http://bootswatch.com/flatly/ -->
        <link href="css/bootstrap.min.css" rel="stylesheet">

        <!-- Custom CSS -->
        <link href="css/styles.css" rel="stylesheet">

        <!-- Custom Fonts -->
        <link href="font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="http://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css">
        <link href="http://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic" rel="stylesheet" type="text/css">

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
                <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
                <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->

</head>

<body id="page-top" class="index">

        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-fixed-top">
                <div class="container">
                        <!-- Brand and toggle get grouped for better mobile display -->
                        <div class="navbar-header page-scroll">
                                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                                        <span class="sr-only">Toggle navigation</span>
                                        <span class="icon-bar"></span>
                                        <span class="icon-bar"></span>
                                        <span class="icon-bar"></span>
                                </button>
                                <a class="navbar-brand" href="#page-top">Magic: The Gathering Cube Tracker</a>
                        </div>

                        <!-- Collect the nav links, forms, and other content for toggling -->
                        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                                <ul class="nav navbar-nav navbar-right">
                                        <li class="hidden">
                                                <a href="#page-top"></a>
                                        </li>
                                        <li class="page-scroll">
                                                <a href="http://plato.cs.virginia.edu/~jjl9ve/ms5/">Go back to Cube</a>
                                        </li>
                                        <li class="page-scroll">
                                            <a href="#" onclick="document.forms['logout'].submit();">Log out of MTG Guestbook</a>
                                        </li>
                                        <form id="logout" action="/logout" method "get">
                                        </form>

                                </ul>
                        </div>
                        <!-- /.navbar-collapse -->
                </div>
                <!-- /.container-fluid -->
        </nav>
    <header>
        <div style="padding-top:60px;padding-left:20px;overflow-y:auto;">
            <p>Welcome %s! Feel free to sign the Guestbook below!</p>
            <form action="/" method="post">
                <div><textarea name="content" rows="3" cols="60"></textarea></div>
                <div><input type="submit" value="Sign Guestbook" class="btn btn-primary"></div>
            </form>
            <div id=contentHolder><ul id=posts>%s</ul></div>
        </div>
    </header>
        <!-- jQuery Version 1.11.0 -->
        <script src="js/jquery-1.11.0.js"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>

        <!-- Plugin JavaScript -->
        <script src="http://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"></script>
        <script src="https://code.jquery.com/ui/1.11.1/jquery-ui.min.js"></script>
        <script src="js/classie.js"></script>
        <script src="js/cbpAnimatedHeader.js"></script>

        <!-- Custom Theme JavaScript -->
        <script src="js/freelancer.js"></script>
    </body>
</html>
"""

#Create cookie to store content
c = Cookie.SimpleCookie()
c['content']=''
c['content']['expires']=False


class MainPage(webapp2.RequestHandler):
    def get(self):
        # [START get_current_user]
        # Checks for active Google account session
        user = users.get_current_user()
        # session = get_current_session()
        # content = session.get('contentHolder', '')
        # [END get_current_user]
        # [START if_user]
        if user:
            self.response.write(main_html % (user, c['content'].value))
        # [END if_user]
        # [START if_not_user]
        else:
            self.redirect(users.create_login_url(self.request.uri))
        # [END if_not_user]
    def post(self):
        user = str(users.get_current_user())
        content = str(c['content'].value) + ('<li><h4 id="contentHeader">' + user + " wrote: " + '</h4>'+ cgi.escape(self.request.get('content')) + '</li>')
        c['content'] = content
        self.redirect('/')
        # session = get_current_session()
        # content = str(session.get('contentHolder')) + ('<li><h4 id="contentHeader">' + user + " wrote: " + '</h4>'+ cgi.escape(self.request.get('content')) + '</li>')
        # session['contentHolder'] = content


class Logout(webapp2.RequestHandler):
    def get(self):
        self.redirect(users.create_logout_url('http://plato.cs.virginia.edu/~jjl9ve/ms5/'))

application = webapp2.WSGIApplication([
    (r'/', MainPage),
    (r'/', MainPage),
    (r'/logout', Logout),
], debug=True)
