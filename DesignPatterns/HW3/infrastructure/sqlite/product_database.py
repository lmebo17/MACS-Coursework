import sqlite3
from uuid import UUID

from core.errors import DoesNotExistError
from core.product import Product


class ProductInDatabase:
    def __init__(self, db_path: str = "./database.db") -> None:
        self.db_path = db_path
        self.create_table()

    def create_table(self) -> None:
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()
            create_table_query = """
                    CREATE TABLE IF NOT EXISTS products (
                        unit_id TEXT,
                        name TEXT,
                        barcode TEXT,
                        price FLOAT,
                        id TEXT
                    )
                """
            cursor.execute(create_table_query)

    def clear_tables(self) -> None:
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()

            truncate_products_query = """
                DELETE FROM products;
            """

            cursor.execute(truncate_products_query)
            connection.commit()

    def create(self, product: Product) -> None:
        insert_query = """
            INSERT INTO products (unit_id, name, barcode, price, id)
            VALUES (?, ?, ?, ?, ?);
        """
        values = (
            str(product.unit_id),
            product.name,
            product.barcode,
            product.price,
            str(product.id),
        )
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()
            cursor.execute(insert_query, values)
            connection.commit()

    def read(self, product_id: UUID) -> Product:
        select_query = """
            SELECT unit_id, name, barcode, price, id FROM products WHERE id = ?;
        """
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()
            cursor.execute(select_query, (str(product_id),))
            row = cursor.fetchone()
            if row:
                return Product(
                    unit_id=UUID(row[0]),
                    name=row[1],
                    barcode=row[2],
                    price=row[3],
                    id=row[4],
                )
            else:
                raise DoesNotExistError(f"No product found with id {product_id}.")

    def read_all(self) -> list[Product]:
        select_all_query = """
            SELECT unit_id, name, barcode, price, id FROM products;
        """
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()
            cursor.execute(select_all_query)
            rows = cursor.fetchall()
            return [
                Product(
                    unit_id=UUID(row[0]),
                    name=row[1],
                    barcode=row[2],
                    price=row[3],
                    id=UUID(row[4]),
                )
                for row in rows
            ]

    def update(self, product_id: UUID, new_price: float) -> None:
        update_query = """
            UPDATE products SET price = ? WHERE id = ?;
        """
        values = (new_price, str(product_id))
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()
            cursor.execute(update_query, values)
            connection.commit()
