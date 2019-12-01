from django.shortcuts import render, get_object_or_404, redirect, reverse
from django.http import HttpResponse, HttpResponseRedirect
from .forms import SearchForm
import json, os, timeit

# Create your views here.
def index(request):
    return HttpResponse("Hello world!")  

def main(request):
    context = dict()
    resultPrint = {}
    if request.method == "POST":
        # get input value
        question = request.POST.get('q')

        # calculate running time
        # start time
        start = timeit.default_timer()

        # remember the input value
        context['content'] = question
        # data processing
        getData = dataprocess()
        dataResult = [i for i in getData if i[0]==question][0]
        result = []

        # get the file list result
        for line in dataResult:
            temp = line.split(', ')
            result.append(temp)

        # get the text line of each result file
        # and store them in to the dictionary
        for f in result[1]:
            text = matchFile(f, question)
            if text[0]:
                resultPrint[f] = text[0][0]

        context['result'] = resultPrint
        
        # end time
        stop = timeit.default_timer()
        # calculate time
        context['time'] = stop - start

        context['length'] = len(resultPrint)

        return render(request, 'result.html', context)

    else:
        return render(request, 'main.html', context)

def index1(request):
    context = dict()
    if request.method == "POST":
        form = SearchForm(request.POST)
        if form.is_valid():
            post_data = form.cleaned_data['q']
            print(post_data)
        context['content'] = "I love UIC"
    else:
        context['content'] = "I love DS!"
        form = SearchForm()
    context['form'] = form
    return render(request, 'index.html', context)

def result(request):
    q = request.GET.get('q')
    context = dict()
    # search={}
    # search['content'] = question
    context['post_data'] = q
    
    if request.method == "GET":
        context['context'] = q
        return render(request, "result.html", context)

    else:
        # file_name = '/home/uic/Desktop/test.txt'
        # indexf = open(file_name,'r')
        # fnames = []
        # while indef.readline():
        #     line = indexf.readline()
        #     context['content'] = "Successfully!"
        
        context['content'] = "I love DS!"
        return render(request, "result.html", context)

def dataprocess():
    filename = '/home/uic/Desktop/final.txt'
    dataset = []
    with open(filename) as f:
        for line in f:
            data1, data2 = line.strip().split(':', 2)
            dataset.append([data1, data2[1:-1]])
    return dataset

def matchFile(doc, word):
    for root, dirs, files in os.walk("/home/uic/search_django/static/RawData", topdown = False):
        for x in files:
            if x == doc:
                path = os.path.join(root, x)
                file1 = open(path)
                lines = file1.readline()

                eachLine = []
                totalLines = []
                while lines:
                    for l in lines.split():
                        if l == word:
                            eachLine.append(lines)
                    lines = file1.readline()

                totalLines.append(eachLine)
                file1.close()

    return totalLines 
