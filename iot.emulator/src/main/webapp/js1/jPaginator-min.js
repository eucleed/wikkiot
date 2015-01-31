(function(b){b.fn.jPaginator=function(y){var f,n,m,h,j,g,k,s,t,l;1!=this.size()&&b.error("You must use this plugin with a unique element");var a={selectedPage:null,nbPages:100,nbVisible:10,widthPx:30,marginPx:1,overBtnLeft:null,overBtnRight:null,maxBtnLeft:null,maxBtnRight:null,withSlider:!0,withAcceleration:!0,speed:2,coeffAcceleration:2,minSlidesForSlider:3,onPageClicked:null};f=0;n=1;g=j=h=m=0;t=s=k=!1;l=!0;return this.each(function(){function B(d){d=1*d.html();c.find(".paginator_p.selected").removeClass("selected");
a.selectedPage=d;b(c.find(".paginator_p_bloc .paginator_p").get(a.selectedPage-n+1)).addClass("selected");a.onPageClicked&&a.onPageClicked.call(this,c,a.selectedPage)}function p(d){c.find(".paginator_p.selected").removeClass("selected");var d=Math.min(a.nbPages-a.nbVisible+1,d),d=Math.max(1,d),e=d-2;c.find(".paginator_p_bloc .paginator_p").each(function(){e+=1;b(this).html(e);a.selectedPage==e&&b(this).addClass("selected")});c.find(".paginator_p_bloc").css("left","-"+f+"px");n=d;j=(d-1)*f;m=0}function q(a){c.find(".paginator_slider").slider();
var a=Math.round(100*(a/h)),e=c.find(".paginator_slider").slider("option","value");a!=e&&c.find(".paginator_slider").slider("option","value",a)}function z(d,e){if(l&&!k){var b=e.value,b=Math.min(100,b),b=Math.max(0,b),c=Math.round(h*b/100)-j;100==b?p(a.nbPages-a.nbVisible+1):0==b?p(1):v(c)}}function v(d){var b=Math.abs(d)/d,u=m+d,b=b*Math.floor(Math.abs(u)/f);m=u%=f;var r=(n-1)*f+m,l=n+b;1>l&&(r=-1);l>a.nbPages&&(r=h+1);0>r?(p(1),m=j=0,q(0),s=!0,g=0,k=!1):r>h?(p(a.nbPages),j=h,m=0,q(h),t=!0,g=0,k=
!1):(t=s=!1,j=r,0!=d&&(0!=b&&p(l),q(j),c.find(".paginator_p_bloc").css("left",-1*u-f+"px")))}function w(a){if(!(t&&"right"==a)&&!(s&&"left"==a)){var b=Math.round(h/10);"left"==a&&(b*=-1);v(b);setTimeout(function(){g+=1;w(a)},20)}}function x(b){if(k){var c=Math.min(Math.abs(a.speed),5),f=Math.min(Math.abs(a.coeffAcceleration),5);a.withAcceleration&&(c=Math.round(c+Math.round(f*g*g/8E4)));"left"==b&&(c*=-1);v(c);setTimeout(function(){g+=1;x(b)},10)}}function A(){var d,e;a.nbVisible=Math.min(a.nbVisible,
a.nbPages);c.find(".paginator_p_bloc > .paginator_p").remove();for(i=1;i<=a.nbVisible+2;i++)c.find(".paginator_p_bloc").append(b("<a class='paginator_p'></a>"));d=a.nbVisible<a.nbPages;a.overBtnLeft&&(d?b(a.overBtnLeft).show():b(a.overBtnLeft).hide());a.overBtnRight&&(d?b(a.overBtnRight).show():b(a.overBtnRight).hide());a.maxBtnLeft&&(d?b(a.maxBtnLeft).show():b(a.maxBtnLeft).hide());a.maxBtnRight&&(d?b(a.maxBtnRight).show():b(a.maxBtnRight).hide());d?(d=Math.ceil(a.nbPages/a.nbVisible),e=a.withSlider,
(e=d<a.minSlidesForSlider?!1:a.withSlider)?(c.find(".paginator_slider").show(),c.find(".paginator_slider").children().show()):(c.find(".paginator_slider").hide(),c.find(".paginator_slider").children().hide())):(c.find(".paginator_slider").hide(),c.find(".paginator_slider").children().hide());d=0;e=c.find(".paginator_p").first().css("border-left-width");0<e.indexOf("px")&&(d=1*e.replace("px",""));f=a.widthPx+2*a.marginPx+2*d;d=1*f*a.nbVisible;c.find(".paginator_p").css("width",a.widthPx+"px");c.find(".paginator_p").css("margin",
"0 "+a.marginPx+"px 0 "+a.marginPx+"px");c.find(".paginator_p_wrap").css("width",d+"px");c.find(".paginator_slider").css("width",d+"px");h=a.nbPages*f-a.nbVisible*f;a.selectedPage=Math.min(a.selectedPage,a.nbPages);p(a.selectedPage-Math.floor((a.nbVisible-1)/2));l=!1;q(j);l=!0;a.selectedPage&&b(c.find(".paginator_p_bloc .paginator_p").get(a.selectedPage-n+1)).addClass("selected");c.find(".paginator_p").bind("click.jPaginator",function(){return B(b(this))})}var c=b(this);y&&b.extend(a,y);A();b(this).bind("reset",
function(c,e){b.extend(a,e);A()});a.withSlider&&(c.find(".paginator_slider").slider({animate:!1}),c.find(".paginator_slider").bind("slidechange.jPaginator",function(a,b){return z(a,b)}),c.find(".paginator_slider").bind("slide.jPaginator",function(a,b){return z(a,b)}),a.nbVisible<a.nbPages&&q(j));a.overBtnLeft&&b(a.overBtnLeft).bind("mouseenter.jPaginator",function(){b(this);k=!0;x("left")});a.overBtnLeft&&b(a.overBtnLeft).bind("mouseleave.jPaginator",function(){b(this);g=0;k=!1});a.overBtnRight&&
b(a.overBtnRight).bind("mouseenter.jPaginator",function(){b(this);k=!0;x("right")});a.overBtnRight&&b(a.overBtnRight).bind("mouseleave.jPaginator",function(){b(this);g=0;k=!1});a.maxBtnLeft&&b(a.maxBtnLeft).bind("click.jPaginator",function(){return w("left")});a.maxBtnRight&&b(a.maxBtnRight).bind("click.jPaginator",function(){return w("right")});c.find(".paginator_p").bind("mouseenter.jPaginator",function(){var a=b(this);c.find(".paginator_p.over").removeClass("over");a.addClass("over")});c.find(".paginator_p").bind("mouseleave.jPaginator",
function(){b(this);c.find(".paginator_p.over").removeClass("over")})})}})(jQuery);