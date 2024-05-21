import sqlite3
from typing import List, Optional, Protocol, Tuple


class IProductDatabase(Protocol):
    def get_cursor(self) -> sqlite3.Cursor:
        pass

    def commit(self) -> None:
        pass

    def close_connection(self) -> None:
        pass

    def fetch_all_products(self) -> List[tuple[str, int]]:
        pass

    def fetch_all_discounts(self) -> List[tuple[str, str, int, float]]:
        pass

    def add_product(self, product_name: str, price_of_single: int) -> None:
        pass

    def add_discount(
        self, product_name: str, single_product_name: str, quantity: int, disc: float
    ) -> None:
        pass

    def get_price(self, product_name: str) -> float:
        pass

    def get_quantity(self, product_name: str) -> int:
        pass

    def get_single_product_name(self, product_name: str) -> str:
        pass

    def get_discount(self, product_name: str) -> float:
        pass

    def add_sold_item(self, product_name: str, table_name: str) -> None:
        pass

    def get_sold_items(self, table_name: str) -> List[tuple[str, int]]:
        pass

    def get_payments(self, table_name: str) -> List[tuple[str, float]]:
        pass

    def add_payment(self, payment: str, money: float, table_name: str) -> None:
        pass

    def get_money(self, payment: str) -> float:
        pass

    def reset_table(self, table_name: str) -> None:
        pass

    def get_total_money(self) -> float:
        pass


class ProductDatabase(IProductDatabase):
    def __init__(self, db_path: str = "store_database.db") -> None:
        self.db_path = db_path
        self.connection: sqlite3.Connection = sqlite3.connect(self.db_path)
        self.cursor: sqlite3.Cursor = self.connection.cursor()

    def get_cursor(self) -> sqlite3.Cursor:
        return self.cursor

    def commit(self) -> None:
        if self.connection:
            self.connection.commit()

    def close_connection(self) -> None:
        if self.connection:
            self.connection.close()

    def fetch_all_products(self) -> List[tuple[str, int]]:
        self.cursor.execute("SELECT * FROM products")
        return self.cursor.fetchall()

    def fetch_all_discounts(self) -> List[tuple[str, str, int, float]]:
        self.cursor.execute("SELECT * FROM discounts")
        return self.cursor.fetchall()

    def add_product(self, product_name: str, price_of_single: int) -> None:
        self.cursor.execute(
            """
            INSERT INTO products (product_name, price_of_single)
            VALUES (?, ?)
        """,
            (product_name, price_of_single),
        )
        self.connection.commit()

    def add_discount(
        self, product_name: str, single_product_name: str, quantity: int, disc: float
    ) -> None:
        self.cursor.execute(
            """
            INSERT INTO discounts (product_name, single_product_name,
            quantity, discount)
            VALUES (?, ?, ?, ?)
        """,
            (product_name, single_product_name, quantity, disc),
        )
        self.connection.commit()

    def get_price(self, product_name: str) -> float:
        self.cursor.execute(
            "SELECT price_of_single FROM products WHERE product_name = ?",
            (product_name,),
        )
        result = self.cursor.fetchone()
        if result:
            return float(result[0])
        return 0.0

    def get_quantity(self, product_name: str) -> int:
        self.cursor.execute(
            "SELECT quantity FROM discounts WHERE product_name = ?", (product_name,)
        )
        result = self.cursor.fetchone()
        if result:
            return int(result[0])
        return 0

    def get_single_product_name(self, product_name: str) -> str:
        self.cursor.execute(
            "SELECT single_product_name FROM discounts WHERE product_name = ?",
            (product_name,),
        )
        result = self.cursor.fetchone()
        if result:
            return str(result[0])
        return ""

    def get_discount(self, product_name: str) -> float:
        self.cursor.execute(
            "SELECT discount FROM discounts WHERE product_name = ?", (product_name,)
        )
        result = self.cursor.fetchone()
        if result:
            return float(result[0])
        return 0.0

    def add_sold_item(self, product_name: str, table_name: str) -> None:
        query = f"SELECT * FROM {table_name} WHERE product_name = ?"
        self.cursor.execute(query, (product_name,))
        existing_item = self.cursor.fetchone()

        if existing_item:
            new_quantity = existing_item[1] + 1
            update_query = (
                f"UPDATE {table_name} SET quantity = ? WHERE product_name = ?"
            )
            self.cursor.execute(update_query, (new_quantity, product_name))
        else:
            insert_query = (
                f"INSERT INTO {table_name} (product_name, quantity) VALUES (?, ?)"
            )
            self.cursor.execute(insert_query, (product_name, 1))

        self.connection.commit()

    def get_sold_items(self, table_name: str) -> List[Tuple[str, int]]:
        query = f"SELECT product_name, quantity FROM {table_name}"
        self.cursor.execute(query)
        sold_items = self.cursor.fetchall()
        return sold_items

    def get_payments(self, table_name: str) -> List[Tuple[str, float]]:
        query = f"SELECT payment, money FROM {table_name}"
        self.cursor.execute(query)
        payments = self.cursor.fetchall()
        return payments

    def add_payment(self, payment: str, money: float, table_name: str) -> None:
        query = f"SELECT * FROM {table_name} WHERE payment = ?"
        self.cursor.execute(query, (payment,))
        existing_payment = self.cursor.fetchone()

        if existing_payment:
            new_money = existing_payment[1] + money
            update_query = f"UPDATE {table_name} SET money = ? WHERE payment = ?"
            self.cursor.execute(update_query, (new_money, payment))
        else:
            insert_query = f"INSERT INTO {table_name} (payment, money) VALUES (?, ?)"
            self.cursor.execute(insert_query, (payment, money))

        self.connection.commit()

    def get_money(self, payment: str) -> float:
        self.cursor.execute(
            "SELECT money FROM payment_statistics WHERE payment = ?", (payment,)
        )
        result = self.cursor.fetchone()

        if result:
            return float(result[0])
        else:
            return 0

    def get_payment_statistics(
        self, payment: str, table_name: str
    ) -> Optional[List[tuple[str, float]]]:
        query = f"SELECT * FROM {table_name} WHERE payment = ?"
        self.cursor.execute(query, (payment,))
        result = self.cursor.fetchall()
        if result:
            return result
        return None

    def reset_table(self, table_name: str) -> None:
        query = f"DELETE FROM {table_name}"
        self.cursor.execute(query)
        self.connection.commit()

    def get_total_money(self) -> float:
        self.cursor.execute("SELECT SUM(money) FROM payment_statistics")
        result = self.cursor.fetchone()
        return float(result[0])
