from flask import Flask, render_template
import sqlite3 as lite
import sys

app = Flask(__name__)


@app.route('/')
def hello_world():
    con = lite.connect('db.db', check_same_thread=False)
    with con:
        cur = con.cursor()
        cur.execute("SELECT * FROM Sotrydniki")
        i = 0
        while True:
            i = i+1
            row = cur.fetchone()

            if row == None:
                break
            if i == 1:
                messages = 'Сотрудник ' + str(i) + '\n' + 'Порядковый номер: ' + row[0] + '\n' + 'Специальность: ' + row[1] + '\n' + 'Занят: ' + row[2]
            else:
                messages = messages + ('\n\nСотрудник ' + str(i) + '\n' + 'Порядковый номер: ' + row[0] + '\n' + 'Специальность: ' + row[1] + '\n' + 'Занят: ' + row[2])

    return render_template('main.html', messages=messages)


@app.route('/main')
def main():
    return 'main'
