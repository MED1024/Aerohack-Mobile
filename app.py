from flask import Flask, render_template
import sqlite3 as lite
import sys

app = Flask(__name__)
global r

@app.route('/')
def hello_world():
    con = lite.connect('db.db', check_same_thread=False)
    with con:
        cur = con.cursor()
        cur.execute("SELECT * FROM Sotrydniki")
        global r
        r = 0
        location1 = [];
        location2 = [];
        while True:
            row = cur.fetchone()

            if row == None:
                break
            #location1[r] = row[3]
            #location2[r] = row[4]
            r = r + 1
            if r == 1:
                messages = 'Сотрудник ' + str(r) + '\n' + 'Порядковый номер: ' + row[0] + '\n' + 'Специальность: ' + row[1] + '\n' + 'Занят: ' + row[2]

            else:
                messages = messages + ('\n\nСотрудник ' + str(r) + '\n' + 'Порядковый номер: ' + row[0] + '\n' + 'Специальность: ' + row[1] + '\n' + 'Занят: ' + row[2])
    return render_template('main.html', messages=messages, location1=location1, location2=location2)


@app.route('/main')
def main():
    return 'main'
