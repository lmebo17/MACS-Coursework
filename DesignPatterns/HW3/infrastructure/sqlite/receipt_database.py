import sqlite3
from uuid import UUID

from core.errors import DoesNotExistError
from core.receipt import Receipt


class ReceiptInDatabase:
    def __init__(self, db_path: str = "./database.db") -> None:
        self.db_path = db_path
        self.create_receipts_table()
        self.create_products_table()

    def create_receipts_table(self) -> None:
        create_table_query = """
            CREATE TABLE IF NOT EXISTS receipts (
                id TEXT PRIMARY KEY,
                status TEXT,
                total_price TEXT
            );
        """
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()
            cursor.execute(create_table_query)

    def create_products_table(self) -> None:
        create_table_query = """
            CREATE TABLE IF NOT EXISTS products_in_receipts (
                receipt_id TEXT,
                product_id TEXT,
                quantity TEXT
            )
        """
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()
            cursor.execute(create_table_query)

    def clear_table(self, table_name: str) -> None:
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()

            truncate_query = f"""
                DELETE FROM {table_name};
            """

            cursor.execute(truncate_query)
            connection.commit()

    def delete(self, receipt_id: UUID) -> None:
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()

            delete_query = """
                DELETE FROM receipts WHERE id = ?;
            """

            cursor.execute(delete_query, (str(receipt_id),))
            connection.commit()

    def change_status(self, receipt: Receipt, new_status: str) -> None:
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()

            update_query = """
                UPDATE receipts SET status = ? WHERE id = ?;
            """

            values = (new_status, str(receipt.id))
            cursor.execute(update_query, values)
            connection.commit()

    def create_receipt(self, receipt: Receipt) -> None:
        print("FFFFFFFFFFFFFFFFFFFFFF")
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()

            insert_query = """
                INSERT INTO receipts (id, status, total_price)
                VALUES (?, ?, ?);
            """

            values = (str(receipt.id), receipt.status, receipt.total_price)
            cursor.execute(insert_query, values)
            connection.commit()

    def read(self, receipt_id: UUID) -> Receipt:
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()

            select_receipt_query = """
                SELECT id, status, total_price
                FROM receipts
                WHERE id = ?;
            """

            cursor.execute(select_receipt_query, (str(receipt_id),))
            receipt_row = cursor.fetchone()

            if not receipt_row:
                raise DoesNotExistError(f"No receipt found with id {receipt_id}.")

            receipt_id, status, total_price = receipt_row
            receipt_id = UUID(str(receipt_id))

            select_products_query = """
                SELECT product_id, quantity
                FROM products_in_receipts
                WHERE receipt_id = ?;
            """

            cursor.execute(select_products_query, (str(receipt_id),))
            product_rows = cursor.fetchall()

        products = {
            UUID(product_id): int(quantity) for product_id, quantity in product_rows
        }

        return Receipt(status, products, total_price, receipt_id)

    def add_product(
        self, receipt_id: UUID, product_id: UUID, quantity: int, price: float
    ) -> None:
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()

            insert_product_query = """
                INSERT INTO products_in_receipts (receipt_id, product_id, quantity)
                VALUES (?, ?, ?);
            """

            product_values = (str(receipt_id), str(product_id), quantity)
            cursor.execute(insert_product_query, product_values)

            update_total_price_query = """
                UPDATE receipts SET total_price = total_price + ? WHERE id = ?;
            """

            update_values = (price, str(receipt_id))
            cursor.execute(update_total_price_query, update_values)

            connection.commit()

    def read_all(self) -> list[Receipt]:
        with sqlite3.connect(self.db_path) as connection:
            cursor = connection.cursor()

            fetch_data_query = """
                SELECT r.id AS receipt_id, r.status,
                r.total_price, p.product_id, p.quantity
                FROM receipts r
                LEFT JOIN products_in_receipts p ON r.id = p.receipt_id;
            """
            cursor.execute(fetch_data_query)
            rows = cursor.fetchall()

            receipt_dict = {}
            for row in rows:
                receipt_id = UUID(row[0])
                status = row[1]
                total_price = float(row[2])

                product_id = row[3]
                quantity = row[4]

                if receipt_id not in receipt_dict:
                    receipt_dict[receipt_id] = {
                        "id": receipt_id,
                        "status": status,
                        "total_price": total_price,
                        "products": {},
                    }

                if product_id:
                    receipt_dict[receipt_id]["products"][UUID(product_id)] = quantity

        return [Receipt(**receipt_data) for receipt_data in receipt_dict.values()]
