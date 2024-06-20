#!/bin/bash

API_URL="https://fuzzy-telegram-jx7jjvxjjrpf5wrw-8080.app.github.dev/api"
#AUTH_TOKEN="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"

declare -a AFFILIATION_IDS
declare -a AUTHOR_IDS
declare -a PUBLISHER_IDS
declare -a BOOK_IDS
declare -a JOURNAL_IDS

# Funkcja do wyodrębniania ID z odpowiedzi JSON
extract_id() {
    echo $1 | jq -r '.id'
}

# Sprawdzenie dostępności endpointu z tokenem uwierzytelnienia
RESPONSE=$(curl -s -L -o /dev/null -w "%{http_code}" -H "Authorization: Bearer $AUTH_TOKEN" "$API_URL/affiliations")
echo "HTTP status code for GET /affiliations: $RESPONSE"

if [ "$RESPONSE" -ne 200 ] && [ "$RESPONSE" -ne 201 ]; then
    echo "Endpoint /affiliations is not available or incorrect. Exiting."
    exit 1
fi

echo "Endpoint /affiliations is available. Continuing with data population."

# Tworzenie 7 afiliacji
for i in {1..7}; do
    RESPONSE=$(curl -s -X POST "$API_URL/affiliations" \
         -H "Content-Type: application/json" \
         -d '{
               "name": "Affiliation '$i'"
             }')
    AFFILIATION_ID=$(extract_id "$RESPONSE")
    if [ -z "$AFFILIATION_ID" ]; then
        echo "Failed to create Affiliation $i"
        exit 1
    fi
    AFFILIATION_IDS+=("$AFFILIATION_ID")
done

echo "Created affiliations: ${AFFILIATION_IDS[@]}"

# Dodanie 10 Author
for i in {1..10}; do
    RANDOM_AFFILATION=${AFFILATION_IDS[$RANDOM % ${#AFFILATION_IDS[@]}]}
    RESPONSE=$(curl -s -X POST "$API_URL/authors" \
         -H "Content-Type: application/json" \
         -d '{
               "surname": "Surname'$i'",
               "name": "Name'$i'",
               "pesel": "1234567890'$i'",
               "score": '$i',
               "affilation": "'$RANDOM_AFFILATION'"
             }')
    AUTHOR_ID=$(extract_id "$RESPONSE")
    if [ -z "$AUTHOR_ID" ]; then
        echo "Failed to create Author $i"
        exit 1
    fi
    AUTHOR_IDS+=("$AUTHOR_ID")
done

# Dodanie 5 Publisher
for i in {1..5}; do
    RESPONSE=$(curl -s -X POST "$API_URL/publishers" \
         -H "Content-Type: application/json" \
         -d '{
               "name": "Publisher '$i'",
               "location": "Location '$i'"
             }')
    PUBLISHER_ID=$(extract_id "$RESPONSE")
    if [ -z "$PUBLISHER_ID" ]; then
        echo "Failed to create Publisher $i"
        exit 1
    fi
    PUBLISHER_IDS+=("$PUBLISHER_ID")
done

# Dodanie 15 Books
for i in {1..15}; do
    RANDOM_AUTHOR=${AUTHOR_IDS[$RANDOM % ${#AUTHOR_IDS[@]}]}
    RANDOM_PUBLISHER=${PUBLISHER_IDS[$RANDOM % ${#PUBLISHER_IDS[@]}]}
    RESPONSE=$(curl -s -X POST "$API_URL/books" \
         -H "Content-Type: application/json" \
         -d '{
               "isbn": "978-3-16-14841'$i'",
               "year": 202'$i',
               "baseScore": 10,
               "title": "Book '$i'",
               "editor": "'$RANDOM_AUTHOR'",
               "publisher": "'$RANDOM_PUBLISHER'"
             }')
    BOOK_ID=$(extract_id "$RESPONSE")
    if [ -z "$BOOK_ID" ]; then
        echo "Failed to create Book $i"
        exit 1
    fi
    BOOK_IDS+=("$BOOK_ID")
done

# Dodanie 10 Journals
for i in {1..10}; do
    RANDOM_PUBLISHER=${PUBLISHER_IDS[$RANDOM % ${#PUBLISHER_IDS[@]}]}
    RESPONSE=$(curl -s -X POST "$API_URL/journals" \
         -H "Content-Type: application/json" \
         -d '{
               "baseScore": 5,
               "title": "Journal '$i'",
               "Issn": "1234-567'$i'",
               "Publisher": "'$RANDOM_PUBLISHER'"
             }')
    JOURNAL_ID=$(extract_id "$RESPONSE")
    if [ -z "$JOURNAL_ID" ]; then
        echo "Failed to create Journal $i"
        exit 1
    fi
    JOURNAL_IDS+=("$JOURNAL_ID")
done

# Dodanie 40 Chapters
for i in {1..40}; do
    RANDOM_BOOK=${BOOK_IDS[$RANDOM % ${#BOOK_IDS[@]}]}
    RANDOM_AUTHORS=$(shuf -e "${AUTHOR_IDS[@]}" -n 3 | jq -R -s -c 'split("\n")[:-1]')
    RESPONSE=$(curl -s -X POST "$API_URL/chapters" \
         -H "Content-Type: application/json" \
         -d '{
               "score": 10,
               "collection": "Collection '$i'",
               "title": "Chapter '$i'",
               "book": "'$RANDOM_BOOK'",
               "authors": '$RANDOM_AUTHORS'
             }')
    if [ -z "$(extract_id "$RESPONSE")" ]; then
        echo "Failed to create Chapter $i"
        exit 1
    fi
done

# Dodanie 40 Articles
for i in {1..40}; do
    RANDOM_JOURNAL=${JOURNAL_IDS[$RANDOM % ${#JOURNAL_IDS[@]}]}
    RANDOM_AUTHORS=$(shuf -e "${AUTHOR_IDS[@]}" -n 3 | jq -R -s -c 'split("\n")[:-1]')
    RESPONSE=$(curl -s -X POST "$API_URL/articles" \
         -H "Content-Type: application/json" \
         -d '{
               "title": "Article '$i'",
               "no": '$i',
               "collection": "Collection '$i'",
               "score": 20,
               "articleNo": 100'$i',
               "vol": '$i',
               "authors": '$RANDOM_AUTHORS',
               "journal": "'$RANDOM_JOURNAL'"
             }')
    if [ -z "$(extract_id "$RESPONSE")" ]; then
        echo "Failed to create Article $i"
        exit 1
    fi
done