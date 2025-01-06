import json
import sys
import xlrd
import re
from openpyxl import Workbook
import json

def init():
    new_dict = {'writer' : ['撰', '著', '注', '疏', '編'], 'source' : ['本', '卷'], 'kind' : ['--']}

    with open("\mode.json", "w") as f:
        json.dump(new_dict, f)

def loadjson():
    with open("mode.json", "r") as load_f:
        load_dict = json.load(load_f)
    writer = load_dict['writer']
    source = load_dict['source']
    kind = load_dict['kind']
    return writer, source, kind

def dumpjson():
    new_dict = {'writer' : writer, 'source' : source, 'kind' : kind}
    with open("mode.json", "w") as f:
        json.dump(new_dict, f)

def output(ans):
    book = Workbook()
    sheet = book.active
    cnt = 0
    ischeck = ['BP', 'BQ', 'BR', 'BS', 'BT', 'BU', 'BV', 'BW','BX','BY','BZ','CA','CB','CC','CD','CE','CF','CG','CH','CI','CJ','CK','CL','CM']
    loc1 = [('A'+chr(i)) for i in range(ord("A"),ord("Z")+1)]
    loc2 = [('B'+chr(i)) for i in range(ord("A"),ord("O")+1)]
    for i in ans:
        cnt = cnt + 1
        sheet["A"+str(cnt)] = i[0]
        sheet["B"+str(cnt)] = i[1]
        sheet["C"+str(cnt)] = i[2]
        sheet["J"+str(cnt)] = i[3]
        sheet["I"+str(cnt)] = i[4]
        sheet["G"+str(cnt)] = i[5]
        sheet["O"+str(cnt)] = i[6]
        sheet["Z"+str(cnt)] = 0
        for j in ischeck:
            sheet[j+str(cnt)] = "否"
        for j in loc1:
            sheet[j+str(cnt)] = 0
        for j in loc2:
            sheet[j+str(cnt)] = 0
        sheet["AF"+str(cnt)] = i[6].find(i[3]) if i[6].find(i[3]) not in [0, -1] else 0
        sheet["AG"+str(cnt)] = i[6].find(i[3]) + len(i[3]) - 1 if i[6].find(i[3]) not in [0, -1] else 0
        sheet["AL"+str(cnt)] = i[6].find(i[4]) if i[6].find(i[4]) not in [0, -1] else 0
        sheet["AM"+str(cnt)] = i[6].find(i[4]) + len(i[4]) - 1 if i[6].find(i[4]) not in [0, -1] else 0
        sheet["AH"+str(cnt)] = i[6].find(i[5]) if i[6].find(i[5]) not in [0, -1] else 0
        sheet["AI"+str(cnt)] = i[6].find(i[5]) + len(i[5]) - 1 if i[6].find(i[5]) not in [0, -1] else 0
        
    book.save('output.xls')

writer, source, kind = loadjson()
while True:
    print('Please choose what you want to do', end = '\n')
    print('0 -- exit  1 -- view the rule  2 -- add a new rule  3 -- split a new book  4 -- init the rule', end = '\n')
    op = input()
    if op == '0':
        sys.exit()
    elif op == '1':
        print('writer : ', writer[:], end = '\n')
        print('source : ', source[:], end = '\n')
        print('kind : ', kind[:], end = '\n')
    elif op == '2':
        print('Please choose what kind of rule you want to add in', end = '\n')
        print('0 -- no  1 -- writer  2 -- source  3 -- kind', end = '\n')
        op = input()
        print(op)
        if op == '0':
            continue
        elif op == '1':
            word = input('Input the rule (like 撰) : ')
            writer.append(word)
            dumpjson()
        elif op == '2':
            word = input('Input the rule (like 本) : ')
            source.append(word)
            dumpjson()
        elif op == '3':
            word = input('Input the rule (like --) : ')
            kind.append(word)
            dumpjson()
    elif op == '3':
        name = input('Please input the book name : ')
        print(name + '.xls')
        wb = xlrd.open_workbook(name + '.xls')
        sh = wb.sheet_by_name('Sheet1')

        whole = zip(sh.col_values(0), sh.col_values(1))
        ans = []
        cnt = -1
        for part in whole:
            cnt += 1
            title = part[0]
            part = part[1]
            if "全文" in part:
                continue

            ssource = ""
            swriter = ""
            skind = ""
            for i in re.split(r'[，。！：？]', part):
                # split the source
                for j in source:
                    if j in i and ssource == "":
                        ssource = i.split(j)[0] + j
                        break
                if len(ssource) > 0:
                    continue
                # split the kind
                for j in kind:
                    if j in i:
                        skind += i
                        break
                if len(skind) > 0:
                    continue
                # split the writer
                for j in writer:
                    if j in i and swriter == "":
                        swriter = i.split(j)[0] + j
                        break
                if len(swriter) > 0:
                    continue
            ans.append([cnt, name, title, swriter, ssource, skind, part])
        output(ans)
    elif op == '4':
        init()