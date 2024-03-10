

import random
import string

ids = 0

def create_user(db):
    global ids
    random_str = ''.join(random.choices(string.ascii_uppercase +string.digits, k=10))
    username = "generated-" + random_str + "-" + str(ids)
    email = "generated@python.com"
    displayed_name = random_str + "-" + str(ids)
    password = "20fe7aba8e6e70eee22cff7eec9c92c877a685fe" # 'user'

    prep = db.cursor()

    query = "INSERT INTO user (email,password,username,displayed_name) VALUES (%s,%s,%s,%s)"
    val = (email,password,username,displayed_name)

    prep.execute(query,val)
    db.commit()

    ids += 1

def delete_generated_users(db):
    prep = db.cursor()
    query = "DELETE FROM user WHERE username LIKE 'generated%'"
    prep.execute(query)
    db.commit()

def get_random_user(db) -> str:
    prep = db.cursor()
    query = "SELECT id FROM user"
    prep.execute(query)
    result = prep.fetchall()
    return str(random.choice(result)[0])
    
def get_random_post(db) -> str:
    prep = db.cursor()
    query = "SELECT id FROM posts"
    prep.execute(query)
    result = prep.fetchall()
    return str(random.choice(result)[0])
    

def create_post(db):
    global ids
    random_str = ''.join(random.choices(string.ascii_uppercase +string.digits, k=100))
    content = str(ids) + " - " + random_str + " - generated"
    date = "2000-01-01"
    user = get_random_user(db)
    parent = -1
    if random.randint(0,3) == 2:
        parent = get_random_post(db)

    prep = db.cursor()

    query = "INSERT INTO posts (content,date,user,parent) VALUES (%s,%s,%s,%s)"
    val = (content,date,user,str(parent))

    prep.execute(query,val)
    db.commit()
    ids += 1


def delete_generated_posts(db):
    prep = db.cursor()
    query = "DELETE FROM posts WHERE date = '2000-01-01'"
    prep.execute(query)
    db.commit()