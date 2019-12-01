from django.urls import path
from django.conf.urls import url
from django.conf.urls.static import static
from django.conf import settings
from . import views



urlpatterns = [
    path('', views.index, name='index'),
    path('demo', views.index1, name='index1'),
    path('main', views.main, name='main'),
    path('result', views.result, name='result'),
]#+ static(sttings.STATIC_URL, document_root=settings.STATIC_ROOT)
