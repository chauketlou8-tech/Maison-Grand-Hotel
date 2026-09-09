import bcrypt, psycopg2

conn = psycopg2.connect(host='localhost',port=3001,database='masongrandhotel',user='postgres',password='TemaSecondary0909@')

cur = conn.cursor()
pw = bcrypt.hashpw('TemaSecondary0909@'.encode('utf-8'), bcrypt.gensalt())
cur.execute('UPDATE users SET password_hash = %s WHERE name = %s', (pw.decode('utf-8'), 'Tlou Elvis Chauke'))
conn.commit()
print('Password updated!')
