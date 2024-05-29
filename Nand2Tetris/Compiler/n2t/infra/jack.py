from __future__ import annotations

from dataclasses import dataclass, field

from n2t.core.compiler.jack_tokenizer import JackTokenizer


@dataclass
class JackProgram:  # TODO: your work for Projects 10 and 11 starts here
    input_path: str = field(default_factory=str)

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> JackProgram:
        print(file_or_directory_name)
        return cls(file_or_directory_name)

    def compile(self) -> None:
        tokenizer = JackTokenizer(input_file=self.input_path)
        pass
