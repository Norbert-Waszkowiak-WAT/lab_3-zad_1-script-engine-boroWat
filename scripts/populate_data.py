import requests
import random
import string
from datetime import datetime
from dateutil import tz

API_URL = "http://127.0.0.1:8080/api"

def random_string(length=10):
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

def current_datetime():
    return (datetime.now(tz.UTC).isoformat())

# Counters for ID generation
counters = {
    "affiliation": 0,
    "author": 0,
    "publisher": 0,
    "book": 0,
    "chapter": 0,
    "journal": 0,
    "article": 0
}

def generate_id(entity_type):
    counters[entity_type] += 1
    return f"{entity_type}{counters[entity_type]}"

def add_affiliation():
    affiliation = {
        "id": generate_id("affiliation"),
        "createDate": current_datetime(),
        "name": random_string(),
        "parent": ""
    }
    response = requests.post(f"{API_URL}/affiliations", json=affiliation)
    print(response.text)
    return response.json()

def add_author(affiliation_id):
    author = {
        "id": generate_id("author"),
        "createDate": current_datetime(),
        "name": random_string(),
        "surname": random_string(),
        "affiliation": affiliation_id
    }
    response = requests.post(f"{API_URL}/authors", json=author)
    print(response.text)
    return response.json()

def add_publisher():
    publisher = {
        "id": generate_id("publisher"),
        "createDate": current_datetime(),
        "name": random_string(),
        "location": random_string()
    }
    response = requests.post(f"{API_URL}/publishers", json=publisher)
    print(response.text)
    return response.json()

def add_book(publisher_id, editor_id):
    book = {
        "id": generate_id("book"),
        "createDate": current_datetime(),
        "isbn": random.randint(1000000000, 9999999999),
        "year": random.randint(1900, 2024),
        "publisher": publisher_id,
        "baseScore": random.randint(1, 100),
        "title": random_string(),
        "editor": editor_id
    }
    response = requests.post(f"{API_URL}/books", json=book)
    print(response.text)
    return response.json()

def add_chapter(book_id, author_ids):
    chapter = {
        "id": generate_id("chapter"),
        "createDate": current_datetime(),
        "authors": author_ids,
        "score": random.randint(1, 100),
        "collection": random_string(),
        "title": random_string(),
        "book": book_id
    }
    response = requests.post(f"{API_URL}/chapters", json=chapter)
    print(response.text)
    return response.json()

def add_journal(publisher_id):
    journal = {
        "id": generate_id("journal"),
        "createDate": current_datetime(),
        "baseScore": random.randint(1, 100),
        "title": random_string(),
        "publisher": publisher_id,
        "issn": random.randint(1000, 9999)
    }
    response = requests.post(f"{API_URL}/journals", json=journal)
    print(response.text)
    return response.json()

def add_article(journal_id, author_ids):
    article = {
        "id": generate_id("article"),
        "createDate": current_datetime(),
        "title": random_string(),
        "no": random.randint(1, 20),
        "year": random.randint(1900, 2024),
        "collection": random_string(),
        "score": random.randint(1, 100),
        "articleNo": random.randint(1, 100),
        "authors": author_ids,
        "vol": random.randint(1, 10),
        "journal": journal_id
    }
    response = requests.post(f"{API_URL}/articles", json=article)
    print(response.text)
    return response.json()

affiliations = [add_affiliation() for _ in range(10)]
publishers = [add_publisher() for _ in range(5)]
authors = [add_author("http://127.0.0.1:8080/api/affiliations/affiliation"+str(random.randint(1, 5))) for _ in range(10)]
books = [add_book("http://127.0.0.1:8080/api/publishers/publisher"+str(random.randint(1, 5)), "http://127.0.0.1:8080/api/authors/author"+str(random.randint(1, 10))) for _ in range(10)]
chapters = [add_chapter("http://127.0.0.1:8080/api/books/book"+str(random.randint(1, 10)), ["http://127.0.0.1:8080/api/authors/author"+str(random.randint(1, 10)) for _ in range(2)]) for _ in range(5)]
journals = [add_journal("http://127.0.0.1:8080/api/publishers/publisher"+str(random.randint(1, 5))) for _ in range(5)]
articles = [add_article("http://127.0.0.1:8080/api/journals/journal"+str(random.randint(1, 5)), ["http://127.0.0.1:8080/api/authors/author"+str(random.randint(1, 10)) for _ in range(2)]) for _ in range(5)]

print("Data population complete.")