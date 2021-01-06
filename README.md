# Text-Search-engine
Based on Hadoop and Django

Project name: Text search Engine based on Hadoop 

Goal: Retrive text context related to the query and order them according to the relavence.

Major components:
1.Text files collection: uses a static crawler to search 99 English articles and 200 MB blog collections. Then he split the text into small articles.
2.Index and Rank:Using local code, hadoop and lucene to index the crawled text. And use hadoop to calculate the value of TF-IDF. Then use python to sort the TF-IDF values ​​and output the results.
3.Web server and interface: to implement web server and interface though djongo


Topics covered： Hadoop, web crawling, text processing, python rank, map-reduce, web frontend and django.

group information:     
Dongna CHEN Linna implement Web server and interface   
Yuchen JIANG Marco implement Text files collection    
Dingtao YANG Young implement index and Rank

BNU-HKBU UIC
2019 Summer
