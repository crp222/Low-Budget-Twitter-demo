import mysql.connector
import sys
from utils import *

db = mysql.connector.connect(
        host="localhost",
        user="root",
        password="",
        database="twitter-clone"
      )

if __name__ == "__main__":
  main_arg = sys.argv[1]
  second_arg = sys.argv[2]

  if main_arg == "-u":
    if second_arg == "-g":
      for i in range(0,100):
            create_user(db)
    elif second_arg == "-d":
      delete_generated_users(db)
  elif main_arg == "-p":
    if second_arg == "-g":
      for i in range(0,100):
            create_post(db)
    elif second_arg == "-d":
      delete_generated_posts(db)

  db.close()