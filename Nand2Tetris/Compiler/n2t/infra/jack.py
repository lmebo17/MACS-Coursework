from __future__ import annotations

import os
from dataclasses import dataclass, field

from n2t.core.compiler.jack_analyzer import JackAnalyzer


def compile_file(file_name: str) -> None:
    analyzer = JackAnalyzer(input_file=file_name)
    analyzer.analyze()


def compile_directory(path: str) -> None:
    jack_files = [f for f in os.listdir(path) if f.endswith(".jack")]
    for file_name in jack_files:
        compile_file(os.path.join(path, file_name))


@dataclass
class JackProgram:  # TODO: your work for Projects 10 and 11 starts here
    input_path: str = field(default_factory=str)

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> JackProgram:
        return cls(file_or_directory_name)

    def compile(self) -> None:
        if os.path.isdir(self.input_path):
            compile_directory(self.input_path)
        else:
            compile_file(self.input_path)
