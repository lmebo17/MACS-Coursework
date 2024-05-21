import sqlite3
from uuid import UUID

from core.errors import DoesNotExistError
from core.unit import Unit


class UnitInDatabase:
    def __init__(self, db_path: str = "./database.db") -> None:
        self.db_path = db_path
        self.create_table()

    def create_table(self) -> None:
        create_table_query = """
            CREATE TABLE IF NOT EXISTS units (
                id TEXT PRIMARY KEY,
                name TEXT
            );
        """
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()
            cursor.execute(create_table_query)

    def clear_tables(self) -> None:
        truncate_units_query = """
            DELETE FROM units;
        """
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()
            cursor.execute(truncate_units_query)
            connection.commit()

    def create(self, unit: Unit) -> None:
        insertion_query = """
            INSERT INTO units (id, name)
            VALUES (?, ?);
        """
        parameters = (str(unit.id), unit.name)
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()
            cursor.execute(insertion_query, parameters)
            connection.commit()

    def read(self, unit_id: UUID) -> Unit:
        select_query = """
            SELECT id, name FROM units WHERE id = ?;
        """
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()
            cursor.execute(select_query, (str(unit_id),))
            row = cursor.fetchone()

        if row:
            return Unit(id=UUID(row[0]), name=row[1])
        else:
            raise DoesNotExistError(f"Unit with id {unit_id} does not exist.")

    def get_all(self) -> list[Unit]:
        fetch_all_query = """
            SELECT id, name FROM units;
        """
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()
            cursor.execute(fetch_all_query)
            unit_records = cursor.fetchall()

        return [Unit(id=UUID(record[0]), name=record[1]) for record in unit_records]
