# CL_Java_workshop_03

## Konfiguracja przed pierwszym uruchomieniem:
1. klasa ``Parameters`` zawiera stałe z parametrami dostępu do servera MySQL:
- ``SQL_DATABASE_NAME`` - domyślnie ``"workshop3"``
- ``SQL_TABLE_NAME`` - domyślnie ``"users"``
- ``SERVLET_CONTEXT`` (kontekst dla servletów, musi być zgodny 
z ustawieniami tomcat'a) - domyślnie ``"/adminpanel"``
2. klasa ``MyDBTools`` zawiera parametry połączenia do servera 
MySQL (najważniejsze to ``DB_USER`` i ``DB_PASS``)
3. klasa ``UserDao`` zawiera stałe ``size_*`` 
określające rozmiar wszystkich pól tabeli

## Inicjowanie bazy danych
Program stworzy bazę danych o ustalonych parametrach 
i doda 4 wpisy początkowe, tylko gdy zostanie uruchomiona 
w trybie konsolowym klasa ``Workshop03`` w pakiecie ``ctrl``. 
W tyrbie servletu usługa oczekuje, że baza jest utworzona 
(może być pusta).
